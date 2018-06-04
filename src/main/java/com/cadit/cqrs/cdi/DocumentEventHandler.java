package com.cadit.cqrs.cdi;

import com.cadit.cqrs.data.DocEvent;
import com.cadit.cqrs.data.KafkaProperties;
import com.cadit.cqrs.kafka.EventConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Properties;
import java.util.logging.Logger;

@Startup
@Singleton
//@WebListener
public class DocumentEventHandler /*implements ServletContextListener*/ {

    public static final String DOC_KAFKA_TOPIC_NAME = "DOC-KAFKA-TOPIC-NAME";
    public static final String DEFAULT_DOC_KAFKA_TOPIC_NAME = "doc-event-handler";
    private static final String DEFAULT_KAFKA_TOPIC_GROUPID = "doc.group1.consumer.kafka";
    public static final String DOC_KAFKA_TOPIC_GROUPID = "DOC_KAFKA_TOPIC_GROUPID";
    private EventConsumer eventConsumer;

    @Resource
    ManagedExecutorService mes;

    Properties kafkaProperties;

    @Inject
    Event<DocEvent> events;

    @Inject
    Configuration conf;


    private Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private String topic_name;


    public void handle(@Observes DocEvent event) {
        log.info("E'' arrivato l'evento dalla topic " + event);
    }


    @PostConstruct
    private void init() {


        topic_name = conf.getConf().getProperty(DOC_KAFKA_TOPIC_NAME, DEFAULT_DOC_KAFKA_TOPIC_NAME);

        Properties consumerkafkaProperties = getKafkaProperties();
        String groupID = conf.getConf().getProperty(DOC_KAFKA_TOPIC_GROUPID, DEFAULT_KAFKA_TOPIC_GROUPID);
        consumerkafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);

        //polling event dalla topic
        eventConsumer = new EventConsumer(consumerkafkaProperties, new ActionWrapper(events), topic_name);

        mes.execute(eventConsumer);
    }


    @Produces
    @KafkaProperties
    public Properties getKafkaProperties() {
        kafkaProperties = new Properties();
        /**** imposto le proprietà comuni a polling consumer e producer kafka***/
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DocEventDeserializer.class.getName());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DocEventSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //        kafkaProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //lista dei server broker del cluster o del server ip:porta dove gira il server kafka (broker)
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaProperties.put("topics.doc", topic_name);
        return kafkaProperties;
    }


    /**
     * Quando si ferma l'app server o si fa l'undeploy il bean viene eliminato
     * però il kafka consumer è attivo in un loop e quindi va fermato
     *
     */
    @PreDestroy
    public void close() {
        log.warning("PRE-DESTROY, TRY TO STOP CONSUMER..");
        eventConsumer.stop();
    }

}
