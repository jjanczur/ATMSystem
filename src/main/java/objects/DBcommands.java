package objects;

import exceptions.ATMaccountException;
import exceptions.BalanceException;

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


}
