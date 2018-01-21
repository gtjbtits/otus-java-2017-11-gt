package com.jbtits.otus.lecture8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class JsonSerializerTest {
    private JsonSerializer mySerializer;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mySerializer = new JsonSerializer();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void serializeEmptyObject () throws JsonProcessingException {
        TestJson emptyObject = new TestJson();
//        String jsonString = mySerializer.toJsonString(emptyObject);
        String jsonString = mapper.writeValueAsString(emptyObject);
        TestJson refurbishedObject;
        try {
            refurbishedObject = mapper.readValue(jsonString, TestJson.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(emptyObject, refurbishedObject);
    }

    private class TestJson {
        public int aInt = 10;
    }
}
