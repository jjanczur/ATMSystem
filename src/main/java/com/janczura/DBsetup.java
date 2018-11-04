package com.janczura;

import com.janczura.objects.ATMuser;
import com.janczura.objects.DB;
import com.janczura.objects.DBcommands;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBsetup {
    private static ResourceBundle rb = ResourceBundle.getBundle("DBconfig");
    private static DB db;
    private static DBcommands dbCommands;
    private static ATMuser atmUser;

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = rb.getString("DB_URL");
    static final String USER = rb.getString("USER");
    static final String PASS = rb.getString("PASS");

    public DBsetup() {
        try {
            setup();
        } catch (SQLException e) {
            System.out.println("Could not setup connection with database");
            e.printStackTrace();
        }
    }

    public static void setup() throws SQLException {
        String sql;
        db = new DB(JDBC_DRIVER, DB_URL, USER, PASS);

        db.loadJdbcDriver();
        db.login();
        dbCommands = new DBcommands(db);
        atmUser = new ATMuser(dbCommands);
    }

    public DB getDb() {
        return db;
    }

    public ATMuser getAtmUser(){
        return atmUser;
    }



    public static void main(String[] args) throws SQLException {
        Menu menu = new Menu();

        menu.displayMenu();

        //Creating table...
        //CREATE TABLE IF NOT EXISTS account (account_id INT, pinCode INT, balance decimal(10,2) unsigned, UNIQUE (account_id));
        //Inserting values
        //"INSERT INTO account VALUES(1, 1111, 50), (2, 2222, 50), (3,3333,50)";
        //Closing connection...
        //db.getStmt().close();
    }



}