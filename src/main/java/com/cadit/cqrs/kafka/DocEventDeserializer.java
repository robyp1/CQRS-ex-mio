package com.cadit.cqrs.kafka;

import com.cadit.cqrs.data.DocEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Converte il messaggio kafka letto da stringa json a oggetto DocEvent  (msg letto dall topic tramite kafka consumer)
 */
public class DocEventDeserializer implements Deserializer<DocEvent> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public DocEvent deserialize(String s, byte[] devBytes) {
        ObjectMapper mapper = new ObjectMapper();
        DocEvent docEvent = null;
        try {
            docEvent = mapper.readValue(devBytes, DocEvent.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return docEvent;
    }

    @Override
    public void close() {

    }
}
