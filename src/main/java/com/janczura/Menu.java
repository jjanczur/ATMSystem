package com.janczura;

import com.janczura.exceptions.ATMaccountException;
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
        System.out.println("3 - Money withdrawal \n");
        System.out.println("100 - Test ATM System \n");

        int[] options = {1, 2, 3, 100};

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
                performWithdrawal();
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



    private void performWithdrawal(){

        Scanner reader = new Scanner(System.in);

        try {
            System.out.println("Enter amount for withdrawal: ");
            Double amount = reader.nextDouble();
            dbSetup.getAtmUser().withdrawal(amount);
            displayMenu();
        }
        catch (Exception e){
            System.out.println("\n You are not logged in.  ");
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
            dbSetup.atmLogin(String.valueOf(login), String.valueOf(pass));
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

    public DBsetup getDBsetup() {
        return dbSetup;
    }

    public void setDBsetup(DBsetup dbSetup) {
        this.dbSetup = dbSetup;
    }

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
