package com.janczura;

import com.janczura.exceptions.ATMaccountException;
import com.janczura.objects.DBcommands;
import com.janczura.test.SystemTest;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {

    private DBsetup dbSetup = new DBsetup();

    public void displayMenu(){
        System.out.println("\n##### ATM SYSTEM #####");
        System.out.println("Press: ");
        System.out.println("1 - Login");
        System.out.println("2 - Display the account balance");
        System.out.println("3 - Money withdrawal ");
        System.out.println("4 - Money deposit ");
        System.out.println("100 - Test ATM System \n");

        int[] options = {1, 2, 3, 4, 5, 100};

        Scanner reader = new Scanner(System.in);

        try {
            System.out.println("Enter a number: ");
            int n = reader.nextInt();
            boolean contains = IntStream.of(options).anyMatch(x -> x == n);

            if(!contains){
                System.out.println("Wrong choice");
                displayMenu();
            }else {
                executeChoice(n);
            }

        }
        catch (Exception e){

            System.out.println(e.getMessage());
            System.out.println("\nWrong value. ");
            displayMenu();
        }
        finally {
            reader.close();
        }

    }

    private  void executeChoice(int n) {
        switch (n){
            case 1:{
                performLogin();
                break;
            }
            case 2:{
                getBalance();
                break;
            }
            case 3:{
                performWithdrawalDeposit(true);
                break;
            }
            case 4:{
                performWithdrawalDeposit(false);
                break;
            }
            case 5:{
                //For testing execute withdrawal without demarcation and balance check
                Scanner reader = new Scanner(System.in);
                System.out.println("Enter amount: ");
                Double amount = reader.nextDouble();
                DBcommands dBcommands = new DBcommands(dbSetup.getDb());
                try {
                    dBcommands.forceWithdrawal(dbSetup.getAtmUser(), amount);
                } catch (SQLException | ATMaccountException e) {
                    System.out.println(e.getMessage());
                }

                break;
            }
            case 100:{
                performSystemTest();
                break;
            }
            default:{
                displayMenu();
                break;
            }
        }
    }



    private void performWithdrawalDeposit(boolean isWithdrawal){

        Scanner reader = new Scanner(System.in);

        try {
            System.out.println("Enter amount: ");
            Double amount = reader.nextDouble();
            if(!isWithdrawal) amount = -amount;
            dbSetup.getAtmUser().withdrawal(amount);
            displayMenu();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            displayMenu();
        }
        finally {
            reader.close();
        }
    }



    private void getBalance() {
        try {
            double balance = dbSetup.getAtmUser().getBalance();
            System.out.println("\n Account balance = " + balance);
            displayMenu();
        } catch (SQLException | ATMaccountException e) {
        System.out.println(e.getMessage());
        displayMenu();
        }
    }

    private void performLogin() {
        Scanner reader = new Scanner(System.in);

        try {
            System.out.println("Enter account id: ");
            int login = reader.nextInt();
            System.out.println("Enter PIN: ");
            int pass = reader.nextInt();
            System.out.println("login = " + login + " pass = " + pass);
            atmLogin(String.valueOf(login), String.valueOf(pass));
            displayMenu();
        }
        catch (Exception e){
            System.out.println("\nWrong value. ");
            displayMenu();
        }
        finally {
            reader.close();
        }
    }

    private void atmLogin(String login, String pass) throws SQLException {
        dbSetup.getAtmUser().setLogin(login);
        dbSetup.getAtmUser().setPass(pass);
        dbSetup.getAtmUser().login();

        if(dbSetup.getAtmUser().isLogged()){
            System.out.println("Login successful");
        }
        else System.out.println("Login unsuccessful. Wrong account id or password. ");
    }


    public DBsetup getDBsetup() {
        return dbSetup;
    }

    public void setDBsetup(DBsetup dbSetup) {
        this.dbSetup = dbSetup;
    }

    /**
     * Method that creates threadsNum threads and run in each withdrawal from ATM account of the user 1
     * To test the db I'm creating 10 different ATM machines with 10 different connections.
     * On each ATM I'm logging in to the first user and I'm trying to withdrawal money from it's account.
     */
    private void performSystemTest() {

        int threadsNum = 10;
        Runnable[] ATMs = new Runnable[threadsNum];
        Thread[] threads = new Thread[threadsNum];
        for (int i = 0; i < threadsNum; i++) {
            ATMs[i] = new SystemTest(i);
        }

        for (int i = 0; i < threadsNum; i++) {
            threads[i] = new Thread(ATMs[i]);
        }
        for (int i = 0; i < threadsNum; i++) {
            threads[i].start();
        }
        displayMenu();
    }
}
