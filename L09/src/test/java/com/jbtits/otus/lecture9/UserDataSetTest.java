package com.jbtits.otus.lecture9;

import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.service.DBService;
import com.jbtits.otus.lecture9.service.DBServiceJDBC;
import com.jbtits.otus.lecture9.utils.reflection.ReflectionHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class UserDataSetTest {
    private DBService dbService;

    @Before
    public void init() {
        dbService = new DBServiceJDBC();
    }

    @After
    public void tearDown() {
        dbService.shutdown();
    }

    @Test
    public void saveAndLoadUserDataSet() {
        UserDataSet user = new UserDataSet();
        user.setName("John Doe");
        user.setAge(30);
        user.setId(10);
        dbService.save(user);
        UserDataSet loaded = dbService.getById(10, UserDataSet.class);
        System.out.println("user: " + user);
        System.out.println("loaded: " + loaded);
        assertTrue(user.equals(loaded));
    }

    @Test
    public void reflectionHelper() {
        UserDataSet user = new UserDataSet();
        user.setName("John Doe");
        user.setAge(30);
        System.out.println(ReflectionHelper.getFields(user));
    }
}
