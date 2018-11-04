package com.janczura.objects;


import com.janczura.exceptions.ATMaccountException;

import java.sql.SQLException;

/**
 * Class that represents the user which is using the ATM. This class executes the commands from Dbcommands, stores the login and password and information if the user that is using the ATM is correctly logged in.
 */
public class ATMuser {
    private String login;
    private String pass;
    private boolean isLogged;
    public DBcommands dBcommands;

    public ATMuser(DBcommands dBcommands) {
        this.dBcommands = dBcommands;
    }

    public void login() throws SQLException {
        isLogged = dBcommands.atmLogin(login, pass);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public double getBalance() throws SQLException, ATMaccountException {
        return dBcommands.getBalance(this);
    }

    public void withdrawal(Double amount) throws SQLException, ATMaccountException {
        dBcommands.moneyWithdrawal(this, amount);
    }

}
