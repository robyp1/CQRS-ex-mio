package com.cadit.cqrs.kafka;

import com.cadit.cqrs.cdi.ActionWrapper;
import com.cadit.cqrs.data.DocEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

/**
 * Ogni consumer ha il suo consumer-ID per consumare da tutte le partizioni
 * Altrimenti specificare le partizioni per consumer:
 *
 *   String topic = "nome della topic";
 *        TopicPartition partition0 = new TopicPartition(topic, 0);
 *        TopicPartition partition1 = new TopicPartition(topic, 1);
 *        consumer.assign(Arrays.asList(partition0, partition1));
 */
public class EventConsumer implements Runnable{

    private final ActionWrapper eventConsumer;
//    private final KafkaConsumer<String, DocEvent> consumer;
    private final KafkaConsumer<String, String> consumer;


    private AtomicBoolean closed = new AtomicBoolean();
    private Logger log = Logger.getLogger(this.getClass().getName());

    public EventConsumer(Properties kafkaProperties, ActionWrapper eventConsumer, String... topics) {
        this.eventConsumer = eventConsumer;
        consumer = new KafkaConsumer(kafkaProperties);
        consumer.subscribe(asList(topics)); //lista delle topic
    }


    @Override
    public void run() {
        try {
            while (!closed.get()) {
                consume();
            }
        } catch (WakeupException e) {
            //  wakeup Exception quando esternamente invoco close sul consumer (vedi stop sotto)
            e.printStackTrace();
        } catch (Exception e) {
            log.severe("si è verificata una Exception che non è una wakeupException! " + e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
            log.info("Consumer stopped! closed: " + closed.get());
        }
    }

    private void consume() {
//        ConsumerRecords<String, DocEvent> records = consumer.poll(Long.MAX_VALUE); //not threadsafe!
        ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE); //not threadsafe!
//        for (ConsumerRecord<String, DocEvent> record : records) {
        for (ConsumerRecord<String, String> record : records) {
            printConsumerInfo(record);
            DocEvent docEvent = new DocEvent(Integer.valueOf(record.value()));
            eventConsumer.accept(docEvent);
        }
        consumer.commitAsync();
//        consumer.commitSync();
    }

//    private void printConsumerInfo(ConsumerRecord<String, DocEvent> record) {
    private void printConsumerInfo(ConsumerRecord<String, String> record) {
        log.info( String.format("*C : Consumer Record:(%s, %s, %d, %d)",
                record.key(), record.value(),
                record.partition(), record.offset()));
    }

    public void stop() {
        log.warning("STOP CONSUMER");
        closed.set(true);
        consumer.wakeup();
    }



    /**
     * funzionerà? Assegno le partizioni a mano per consumer.
     * Come assegno invece
     * dal producer la partizione a cui scrivere
     * @param topics
     */
//    public void assignPartitionperTopicToConsumer(String... topics){
//        List<String> topicList = (List<String>) asList(topics);
//        //lista delle partizioni: http://cloudurable.com/blog/kafka-tutorial-kafka-producer-advanced-java-examples/index.html
//        for (String topic : topicList){
//            TopicPartition partitionPerEvento1 = new TopicPartition(topic, 0);
//            TopicPartition partitionPerEvento2 = new TopicPartition(topic, 1);
//            consumer.assign(asList(partitionPerEvento1, partitionPerEvento2));
//        }
//    }

}
