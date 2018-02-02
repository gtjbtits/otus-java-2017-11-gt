package com.jbtits.otus.lecture8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;

public class JsonSerializerTest {
    private JsonSerializer serializer;
    private ObjectMapper mapper;

    private <T> T deserialize(String json, Class<T> targetClass) {
        T deserialized;
        try {
            deserialized = mapper.readValue(json, targetClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deserialized;
    }

    @Before
    public void setUp() {
        serializer = new JsonSerializer();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void canSerializePrimitiveList() {
        List<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("green");
        colors.add("blue");
        String json = serializer.toJson(colors);
        List<String> dColors = (List<String>) deserialize(json, List.class);
        assertTrue(colors.equals(dColors));
    }

    @Test
    public void canSerializePrimitiveMap() {
        Map<String, Integer> coordinates = new HashMap<>();
        coordinates.put("X", 10);
        coordinates.put("Y", 0);
        coordinates.put("Z", -5);
        String json = serializer.toJson(coordinates);
        Map<String, Integer> dCoordinates = (Map<String, Integer>) deserialize(json, Map.class);
        assertTrue(coordinates.equals(dCoordinates));
    }

    @Test
    public void canSerializeBagOfObjects() {
        Bag bag = new Bag();
        double doubles[] = {1.1, 2.2, 3.3};
        String strings[] = {"abc", "def", "jhk"};
        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        List<String> list = new LinkedList<>();
        list.add("l1");
        list.add("l2");
        list.add("l3");
        Set<String> set = new HashSet<>();
        set.add("s1");
        set.add("s2");
        set.add("s3");
        bag.setDoubles(doubles);
        bag.setStrings(strings);
        bag.setMap(map);
        bag.setList(list);
        bag.setQueue((LinkedList) list);
        bag.setSet(set);
        String json = serializer.toJson(bag);
        System.out.println(json);
        Bag dBag = deserialize(json, Bag.class);
        assertTrue(dBag.equals(bag));
    }
}
