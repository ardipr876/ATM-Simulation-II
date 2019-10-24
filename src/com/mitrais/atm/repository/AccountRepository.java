package com.mitrais.atm.repository;

import com.mitrais.atm.helpers.CsvHelper;
import com.mitrais.atm.models.AccountModel;
import com.mitrais.atm.repository.implement.IAccountRepository;
import com.mitrais.atm.screens.enums.FileTypeEnum;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Account Repository
 * @author Ardi_PR876
 */
public class AccountRepository implements IAccountRepository {
    private static AccountRepository INSTANCE;
    private final String accountCsv = CsvHelper.getPropValue("directoryCsv") + "\\account.csv";

    private AccountRepository() {
        
    }
    
    public static AccountRepository getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new AccountRepository();
        }
        return INSTANCE;
    }
    
    /**
     * Get Account List
     * @param path
     * @return 
     */
    @Override
    public List<AccountModel> getAccountList(String path) {
        List<AccountModel> accounts = new ArrayList<>();
        
        List<List<String>> lines = CsvHelper.readFromCSV(path, FileTypeEnum.ACCOUNT.name());
        
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
        
        String pin = metadata.get(1);
        
        float balance = Float.parseFloat(metadata.get(2));
        
        String accountNumber = metadata.get(3);

        return new AccountModel(name, accountNumber, pin, balance);
    }
    
    /**
     * Update Account Data
     * @param accounts 
     */
    @Override
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
                pw.append(current.getPin());
                pw.append(",");
                pw.append(Float.toString(current.getBalance()));
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
