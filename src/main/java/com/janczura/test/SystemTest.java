package com.janczura.test;

import com.janczura.exceptions.ATMaccountException;
import com.janczura.objects.ATMuser;
import com.janczura.objects.DB;
import com.janczura.objects.DBcommands;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class SystemTest implements Runnable{

    private int atmId;
    //private DBsetup atm = new DBsetup();
    private  ResourceBundle rb = ResourceBundle.getBundle("DBconfig");
    private  DB db;
    private  DBcommands dbCommands;
    private  ATMuser atmUser;

     private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
     private final String DB_URL = rb.getString("DB_URL");
     private final String USER = rb.getString("USER");
     private final String PASS = rb.getString("PASS");


    private  void setup() throws SQLException {
        db = new DB(JDBC_DRIVER, DB_URL, USER, PASS);
        db.loadJdbcDriver();
        db.login();
        dbCommands = new DBcommands(db);
        atmUser = new ATMuser(dbCommands);
    }


    public SystemTest(int atmId) {
        this.atmId = atmId;
        try {
            setup();
            atmUser.setLogin("1");
            atmUser.setPass("1111");
            atmUser.login();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        System.out.println("ATM ID = " + atmId);
        try {
            atmUser.withdrawal(5.0);
        } catch (SQLException | ATMaccountException e) {
            e.printStackTrace();
        }
    }
}
