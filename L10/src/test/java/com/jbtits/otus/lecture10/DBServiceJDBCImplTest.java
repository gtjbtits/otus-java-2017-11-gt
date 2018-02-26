package com.jbtits.otus.lecture10;

import com.jbtits.otus.lecture10.dataSets.UserDataSet;
import com.jbtits.otus.lecture10.dbService.DBService;
import com.jbtits.otus.lecture10.dbService.DBServiceJDBCImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DBServiceJDBCImplTest {
    private DBService dbService;

    @Before
    public void init() {
        dbService = new DBServiceJDBCImpl();
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
        dbService.saveUser(user);
        UserDataSet loaded = dbService.getUserById(10);
        System.out.println("user: " + user);
        System.out.println("loaded: " + loaded);
        assertTrue(user.equals(loaded));
    }
}
