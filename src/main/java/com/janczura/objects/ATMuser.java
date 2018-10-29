package com.janczura.objects;


import com.janczura.exceptions.ATMaccountException;

import java.sql.SQLException;

public class ATMuser {
    private String login;
    private String pass;
    private String balance;
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

    public String getPass() {
        return pass;
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

    public DBcommands getdBcommands() {
        return dBcommands;
    }

    public void setdBcommands(DBcommands dBcommands) {
        this.dBcommands = dBcommands;
    }
}
