package com.jbtits.otus.lecture15;

import com.jbtits.otus.lecture15.front.webSocket.UUID;
import com.jbtits.otus.lecture15.front.webSocket.messages.Action;
import com.jbtits.otus.lecture15.utils.reflection.ReflectionUtils;
import com.jbtits.otus.lecture15.utils.reflection.SimpleField;
import org.junit.Test;

import java.util.List;

public class ReflectionTest {
    @Test
    public void collectAllFields() {
        Action action = new Action(UUID.UNKNOWN, "test");
        List<SimpleField> fields = ReflectionUtils.getFields(action, Action.class);
        System.out.println(fields);
    }
}
