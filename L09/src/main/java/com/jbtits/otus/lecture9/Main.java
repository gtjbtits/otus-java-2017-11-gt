package com.jbtits.otus.lecture9;

import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.service.DBService;
import com.jbtits.otus.lecture9.service.DBServiceJDBC;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBServiceJDBC();
        UserDataSet user = new UserDataSet();
        user.setName("John Doe");
        user.setAge(30);
        dbService.prepareTables();
        dbService.saveUser(user);
        UserDataSet selected = dbService.getUserById(1);
        System.out.println(selected);
        dbService.shutdown();
    }
}
