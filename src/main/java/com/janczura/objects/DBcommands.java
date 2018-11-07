package com.janczura.objects;

import com.janczura.exceptions.ATMaccountException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that executes whole queries to DB. Methods from this class are used by ATMuser. All of the methodes are SQLInjection safe due to the correct usage of PreparedStatement
 */
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

    /**
     * method that execute select query to find out if account id and pin are  correct. If they are the method returns true otherwise false
     * @param userid - account id in database
     * @param password - user PIN in database
     * @return are userid and password stored in db?
     * @throws SQLException
     */

    public boolean atmLogin(String userid, String password) throws SQLException {
        PreparedStatement stmt = db.getConn().prepareStatement("SELECT * FROM account WHERE account_id=? AND pinCode=?");
        stmt.setString(1, userid);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    /**
     *
     * @param atmUser
     * @return
     * @throws SQLException
     * @throws ATMaccountException
     */
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

    public void forceWithdrawal(ATMuser atmUser, Double amount) throws SQLException, ATMaccountException {

        if(atmUser.isLogged()) {
            System.out.println("Before account operation: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
            try(PreparedStatement stmt = db.getConn().prepareStatement("UPDATE account SET balance=balance - ? WHERE account_id=?")) {

                stmt.setString(1, amount.toString());
                stmt.setString(2, atmUser.getLogin());
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("After account operation: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
        }
        else throw new ATMaccountException("ATM user is not logged in. To see the balance, login first.");
    }

    /**
     * Methode to perform withdrawal operation.
     * 1. Check if user is correctly logged in.
     * 2. Perform sql update query - money withdrawal
     * 3. Since we are in strickt mode and balance column is unsigned any update that may result in negative balance will throw an exception.
     * So setting up negative balance is equal to trying update decimal column with string value - the exception will be thrown and the transaction will be rolled back
     * Methode is SQLinjection safe due to usage of PreparedStatement
     * @param atmUser Object that represents user
     * @param amount Amount of money to withdrawal from users bank account
     * @throws SQLException Problem with sql statement execution
     * @throws ATMaccountException Exception it inform about obligatory login
     */
    public void moneyWithdrawal(ATMuser atmUser, Double amount) throws SQLException, ATMaccountException {

        if(atmUser.isLogged()) {
            System.out.println("Before account operation: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
            try {
                db.enableDemarcation();

                PreparedStatement stmt = db.getConn().prepareStatement("UPDATE account SET balance=balance - ? WHERE account_id=?");
                stmt.setString(1, amount.toString());
                stmt.setString(2, atmUser.getLogin());
                stmt.executeUpdate();

                db.getConn().commit();

                stmt.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Rollback demarcated transactions.");
                db.getConn().rollback();
            }
            finally {
                db.disableDemarcation();
            }
            System.out.println("After account operation: User account - " + atmUser.getLogin() + " balance - " + atmUser.getBalance());
        }
        else throw new ATMaccountException("ATM user is not logged in. To see the balance, login first.");
    }





}
