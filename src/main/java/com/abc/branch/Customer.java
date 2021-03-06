package com.abc.branch;

import com.abc.account.Account;
import com.abc.transaction.Transaction;
import com.abc.transaction.TransactionType;
import com.abc.util.ZeroAmountException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Customer {
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    /**
     * Make a transfer between two <code>Account</code> owned by this <code>Customer</code>.
     * @param from account id to transfer balance from
     * @param to account id to transfer balance to
     * @return <code>true</code> if transfer was successful
     */
    public boolean transferByID(double amount, String from, String to) {

        Account acFrom = searchAccount(from);
        Account acTo = searchAccount(to);

        if( (acFrom != null) && (acTo != null) ) {
            try {
                acFrom.withdraw(amount);
                acTo.deposit(amount);
                return true;
            } catch (ZeroAmountException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    private Account searchAccount(String accountId) {
        for (Account a : this.accounts) {
            if (a.getId().equals(accountId)) {
                return a;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Customer addAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() {
        double total = 0;
        for (Account a : accounts)
            total += a.interestEarned();
        return total;
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + statementForAccount(a) + "\n";
            total += a.getBalance();
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

       // Translate to pretty account type
        switch(a.getAccountType()){
            case CHECKING:
                s += "Checking Account\n";
                break;
            case SAVINGS:
                s += "Savings Account\n";
                break;
            case MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        // Now total up all the transactions
        double total = 0.0;
        for (Transaction t : a.getTransactions()) {
            if (t.getType() == TransactionType.CREDIT) {
                s += "  " + "deposit" + " " + toDollars(t.getAmount()) + "\n";
            }
            if (t.getType() == TransactionType.DEBIT) {
                s += "  " + "withdrawal" + " " + toDollars(t.getAmount()) + "\n";
            }
        }
        s += "Total " + toDollars(a.getBalance());
        return s;
    }

    private String toDollars(double d){
        return String.format("$%,.2f", abs(d));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return name.equals(customer.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
