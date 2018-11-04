package com.janczura.objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private String JDBC_DRIVER;
    private String DB_URL;
    private String user;
    private String pass;
    private Connection conn = null;
    private Statement stmt = null;

    public DB(String JDBC_DRIVER, String DB_URL, String user, String pass) {
        this.JDBC_DRIVER = JDBC_DRIVER;
        this.DB_URL = DB_URL;
        this.user = user;
        this.pass = pass;


    }

    public void loadJdbcDriver(){
        System.out.println("Load MySQL JDBC driver");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Methode that login the user to db
     * To keep high isolation of transaction even though the default transaction isolation level for MySQL db is REPEATABLE_READ was changed to SERIALIZABLE
     *To force db to throw SQLException when client tries to update balance with negative values (even though positive balance is checked during each witdrawal otherwise transaction is rolledback) sql_mode = 'STRICT_TRANS_TABLES’ is eabled
     * @throws SQLException
     */
    public void login() throws SQLException {
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, user, pass);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        conn.prepareStatement("SET @@SESSION.sql_mode = 'STRICT_TRANS_TABLES';").execute();
    }

    public void createStatement() throws SQLException {
        System.out.println("Creating statement...");
        stmt = conn.createStatement();
    }

    /**
     * Enable demarcation mechanism which is a base for keeping atomicity of each update transaction

     * @throws SQLException
     */
    public void enableDemarcation() throws SQLException{
        //enable transaction demarcation
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
    }

    /**
     * Disable demarcation mechanism which is a base for keeping atomicity of each update transaction

     * @throws SQLException
     */
    public void disableDemarcation() throws SQLException{
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
    }

    public String getJDBC_DRIVER() {
        return JDBC_DRIVER;
    }
    public String getDB_URL() {
        return DB_URL;
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }
}
