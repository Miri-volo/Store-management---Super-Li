package groupk.workers.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankAccount {
    private String bank;
    private int bankID;
    private int bankBranch;


    public void setBankAccount(String id, String bank, int bankID, int bankBranch) {
        try {
            PreparedStatement preparedStatement = DalController.connection.prepareStatement("UPDATE Employee set BankName = ? where ID = ?");
            preparedStatement.setString(1, bank);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            PreparedStatement preparedStatement2 = DalController.connection.prepareStatement("UPDATE Employee set BankBranch = ? where ID = ?");
            preparedStatement2.setInt(1, bankBranch);
            preparedStatement2.setString(2, id);
            preparedStatement2.executeUpdate();
            PreparedStatement preparedStatement3 = DalController.connection.prepareStatement("UPDATE Employee set BankID = ? where ID = ?");
            preparedStatement3.setInt(1, bankID);
            preparedStatement3.setString(2, id);
            preparedStatement3.executeUpdate();
            this.bank = bank;
            this.bankID = bankID;
            this.bankBranch = bankBranch;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }
    public BankAccount(String bank, int bankID, int bankBranch){
        this.bank = bank;
        this.bankID = bankID;
        this.bankBranch = bankBranch;

    }

    public String getBank() {
        return bank;
    }
    public int getBankID() { return bankID; }
    public int getBankBranch() {
        return bankBranch;
    }

}
