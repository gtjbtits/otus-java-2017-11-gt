package com.jbtits.otus.lecture15;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JacksonTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void excessFieldDoesNotThrowsException() throws IOException {
        mapper.readValue("{\"action\": \"test\", \"other_field\": \"1\"}", Action.class);
    }

    @Test
    public void requiredTest() throws IOException {
        Action res = mapper.readValue("{\"action\": \"test\"}", Action.class);
//        Action res = mapper.readValue("{}", Action.class);
        System.out.println(res.getTimestamp());
    }

    @Test
    public void rewriteFieldsTest() throws IOException {
        Action res = mapper.readValue("{\"ts\": \"1000\", \"a\": \"test\"}", Action.class);
//        Action res = mapper.readValue("{}", Action.class);
        System.out.println(res.getTimestamp());
    }
}
