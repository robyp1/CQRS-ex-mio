package com.cadit.cqrs.business;

import com.cadit.cqrs.cdi.DocumentCreator;
import com.cadit.cqrs.data.*;
import com.cadit.cqrs.kafka.EventProducer;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * *************************************************************************************************
 * ********************************************BACK-END*********************************************
 * *************************************************************************************************
 */
@Stateless
public class DocumentCreatorBean {

    @EJB
    DocumentCreator documentCreator;

    @Inject
    EventProducer eventProducer;

    @Resource
    SessionContext ejbSessionContext;

    @PersistenceContext
    EntityManager em;

    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    /**
     * Se il documento è stato creato salva l'evento e invia l'evento al front end
     */
    @Asynchronous
    //qui non metto @TransactionAttribute(TransactionAttributeType.REQUIRED) perchè è il default per un bean
    public void pdfCreator(){
        EventEntity finalEventState = new EventEntity();
        try {
            DocEnum type = DocEnum.PDF;
            Integer docId = documentCreator.create(type);//oggetto detached perchè appartiene ad una altra transazione
            DocumentEntity documentEntity = em.find(DocumentEntity.class, docId);
            finalEventState.setType(type);
            finalEventState.setStatus(StatusJob.COMPLETED);
            finalEventState.setInfoPath(documentEntity.getPath());
            documentEntity.addEvent(finalEventState);
            em.persist(finalEventState);

            DocEvent docEvent = new DocEvent(documentEntity.getId());
            eventProducer.publish(docEvent);

            //            throw new RuntimeException("Ops! runtime ex forzata per provare che ha terminato la transazione interna e ha fatto rollback solo di questa");
            //quella interna salva cmq i riferimenti al pdf che è stato completato, ci perdiamo solo l'evento di comunicazione al front-end

        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e); //usare log.error o log.warning, eventualmente rilanciare exc o una RuntimeExc
            ejbSessionContext.setRollbackOnly(); //se il pdf non si è cereato forzo rollback senza rilanciare Runtime che lo farebbe
        }
    }


    public void removeAllEventsByDocument(Integer docId){
        DocumentEntity documentEntity = em.find(DocumentEntity.class, docId);
        for (EventEntity ev : documentEntity.getEventIds()) {
            em.remove(ev); //non usando cascade delete scelgo quali cancellare, qui tutte quindi sotto faccio clear
        }
        documentEntity.getEventIds().clear();//rimuovo dalla lista tutto
        em.merge(documentEntity);
    }

}
