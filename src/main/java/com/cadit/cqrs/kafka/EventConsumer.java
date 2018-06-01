package com.cadit.cqrs.kafka;

import com.cadit.cqrs.cdi.ActionWrapper;
import com.cadit.cqrs.data.DocEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static java.util.Arrays.asList;


public class EventConsumer implements Runnable{

    private final ActionWrapper eventConsumer;
//    private final KafkaConsumer<String, DocEvent> consumer;
    private final KafkaConsumer<String, String> consumer;


    private AtomicBoolean closed = new AtomicBoolean();
    private Logger log = Logger.getLogger(this.getClass().getName());

    public EventConsumer(Properties kafkaProperties, ActionWrapper eventConsumer, String... topics) {
        this.eventConsumer = eventConsumer;
        consumer = new KafkaConsumer(kafkaProperties);
        consumer.subscribe(asList(topics));
    }


    @Override
    public void run() {
        try {
            while (!closed.get()) {
                consume();
            }
        } catch (WakeupException e) {
            // will wakeup for closing
            e.printStackTrace();
        } catch (Exception e) {
            // will wakeup for closing
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

}
