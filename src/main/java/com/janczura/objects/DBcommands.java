package com.janczura.objects;

import com.janczura.exceptions.ATMaccountException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcommands {

    private DB db;

    public DBcommands(DB db) {
        this.db = db;
    }

    public DB getDb() {
        return db;
    }
    public void setDb(DB db) {
        this.db = db;
    }

    /*    PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE userid=? AND password=?");
    stmt.setString(1, userid);
    stmt.setString(2, password);
    ResultSet rs = stmt.executeQuery();*/

    public boolean atmLogin(String userid, String password) throws SQLException {
        PreparedStatement stmt = db.getConn().prepareStatement("SELECT * FROM account WHERE account_id=? AND pinCode=?");
        stmt.setString(1, userid);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public double getBalance(ATMuser atmUser) throws SQLException, ATMaccountException {
        if(atmUser.isLogged()) {
            PreparedStatement stmt = db.getConn().prepareStatement("SELECT balance FROM account WHERE account_id=?");
            stmt.setString(1, atmUser.getLogin());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getDouble("balance");
        }
        else throw new ATMaccountException("ATM user is not logged in. To see the balance, login first.");
    }

    /**
     * Methode to perform withdrawal operation. Demarcation is used to assure that balance after withdrawal is > 0. If not transaction is rolled back.
     * Methode is SQLinjection safe due to usage of PreparedStatement
     * @param atmUser Object that represents user
     * @param amount Amount of money to withdrawal from users bank account
     * @throws SQLException Problem with sql statement execution
     * @throws ATMaccountException Exception it inform about obligatory login
     */
    public void moneyWithdrawal(ATMuser atmUser, Double amount) throws SQLException, ATMaccountException {

        if(atmUser.isLogged()) {
            System.out.println("Before withdrawal: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
            try {
                db.enableDemarcation();

                PreparedStatement stmt = db.getConn().prepareStatement("UPDATE account SET balance=balance - ? WHERE account_id=1");
                stmt.setString(1, amount.toString());
                stmt.executeUpdate();

                if(atmUser.getBalance() < 0){
                    System.out.println("The account balance is not sufficient to perform this operation");
                    db.getConn().rollback();
                }

                db.getConn().commit();
            } catch (SQLException e) {
                System.out.println("Rollback demarcated transactions.");
                db.getConn().rollback();
            }
            finally {
                db.disableDemarcation();
            }
            System.out.println("After withdrawal: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
        }
        else throw new ATMaccountException("ATM user is not logged in. To see the balance, login first.");
    }




}
