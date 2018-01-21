package com.jbtits.otus.lecture8;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonSerializer {
    public String toJsonString(Object emptyObject) {
        JSONObject obj = new JSONObject();
        return obj.toJSONString();
    }
}
