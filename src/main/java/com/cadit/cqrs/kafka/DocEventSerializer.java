package com.cadit.cqrs.kafka;

import com.cadit.cqrs.data.DocEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Converte in stringa di json l'oggetto DocEvent che deve essere incluso nel messaggio da
 * inviare alla topic tramite kafka
 */
public class DocEventSerializer implements Serializer<DocEvent>{


    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, DocEvent docEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] serializedBytes =null;
        try {
            serializedBytes = objectMapper.writeValueAsString(docEvent).getBytes();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serializedBytes;
    }

    @Override
    public void close() {

    }
}
