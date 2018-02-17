package com.jbtits.otus.lecture9;

import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.service.DBService;
import com.jbtits.otus.lecture9.service.DBServiceJDBC;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBServiceJDBC();
        try {
            dbService.prepareTables();
            dbService.saveUser(new UserDataSet());
            dbService.getUserById(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.shutdown();
    }
}
