package com.jbtits.otus.lecture10;

import com.jbtits.otus.lecture10.dataSets.AddressDataSet;
import com.jbtits.otus.lecture10.dataSets.PhoneDataSet;
import com.jbtits.otus.lecture10.dataSets.UserDataSet;
import com.jbtits.otus.lecture10.dbService.DBService;
import com.jbtits.otus.lecture10.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture10.dbService.DBServiceJDBCImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class DBServiceHibernateImplTest {
    private DBService dbService;

    @Before
    public void init() {
        dbService = new DBServiceHibernateImpl();
    }

    @After
    public void tearDown() {
        dbService.shutdown();
    }

    @Test
    public void saveAndLoadUserDataSet() {
        AddressDataSet address = new AddressDataSet();
        address.setStreet("B. Bronnaya st.");

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setNumber("010");
        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setNumber("011");
        List<PhoneDataSet> phones = Arrays.asList(phone1, phone2);

        UserDataSet user = new UserDataSet();
        user.setName("John Doe");
        user.setAge(30);
        user.setAddress(address);
        user.setPhones(phones);

        dbService.saveUser(user);
        UserDataSet loaded = dbService.getUserById(1);
        System.out.println("user: " + user);
        System.out.println("loaded: " + loaded);
        assertTrue(user.equals(loaded));
    }
}
