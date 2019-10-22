package com.mitrais.atm.services;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.services.implement.IAccountService;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Account Service
 * @author Ardi_PR876
 */
public class AccountService implements IAccountService {
    private static AccountService INSTANCE;
    private final String userDir = System.getProperty("user.dir");
    private final String accountCsv = this.userDir + "\\File\\account.csv";
    
    private AccountService(){
        
    }
    
    /**
     * Singleton Account Service
     * @return AccountService INSTANCE
     */
    public static AccountService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new AccountService();
        }
        return INSTANCE;
    }
    
    /**
     * get account list from CSV file
     * @return List Account 
     */
    @Override
    public List<AccountModel> getAccountList() {
        List<List<String>> lines = CsvHelper.readFromCSV(this.accountCsv);
        
        List<AccountModel> accounts = new ArrayList<>();
        
        lines.stream().map((line) -> createAccountObject(line)).forEach((account) -> {
            accounts.add(account);
        });

        return accounts;
    }
    
    /**
     * Create Account object from CSV line
     * @param metadata
     * @return AccountModel
     */
    private AccountModel createAccountObject(List<String> metadata) {
        String name = metadata.get(0);
        
        float balance = Float.parseFloat(metadata.get(1));
        
        String pin = metadata.get(2);
        
        String accountNumber = metadata.get(3);

        return new AccountModel(name, accountNumber, pin, balance);
    }
    
    public void updateAccountData(List<AccountModel> accounts) {
        try (FileWriter pw = new FileWriter(this.accountCsv)) {
            pw.append("Name");
            pw.append(",");
            pw.append("Balance");
            pw.append(",");
            pw.append("PIN");
            pw.append(",");
            pw.append("Account Number");
            pw.append("\n");
                
            Iterator s = accounts.iterator();
            
            if (s.hasNext() == false) {
                System.out.println("Empty");
            }

            while (s.hasNext()) {
                AccountModel current = (AccountModel)s.next();

                pw.append(current.getName());
                pw.append(",");
                pw.append(Float.toString(current.getBalance()));
                pw.append(",");
                pw.append(current.getPin());
                pw.append(",");
                pw.append(current.getAccountNumber());
                pw.append("\n");
            }

            pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
