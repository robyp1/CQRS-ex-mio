package com.cadit.cqrs.cdi;

import com.cadit.cqrs.data.DocumentEntity;
import com.cadit.cqrs.data.EventEntity;
import com.cadit.cqrs.data.DocEnum;
import com.cadit.cqrs.data.StatusJob;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * *************************************************************************************************
 * ********************************************BACK-END*********************************************
 * *************************************************************************************************
 */
@Stateless
public class DocumentCreator {

    @PersistenceContext
    EntityManager em;

    /**
     * Salva i riferimenti al documento creato in una transazione a parte
     * @param docType
     * @return
     * @throws InterruptedException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer create(DocEnum docType) throws InterruptedException {
        String infoPath ="";
        EventEntity initialEventState = new EventEntity();
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setDocType(docType);
        switch (docType){
            case PDF :  initialEventState.setStatus(StatusJob.RUNNING);
                initialEventState.setType(docType);
                Thread.sleep(5000);
                infoPath = "path/to/pdf/" + "1";
                break;
            case EXCEL:
                initialEventState.setStatus(StatusJob.RUNNING);
                initialEventState.setType(docType);
                Thread.sleep(58000);
                infoPath = "path/to/excel/" + "1";
                break;
        }
        initialEventState.setInfoPath(infoPath);
        documentEntity.addEvent(initialEventState);
        documentEntity.setPath(infoPath);
        em.persist(documentEntity);
        em.flush();
        return  documentEntity.getId();

    }
}
