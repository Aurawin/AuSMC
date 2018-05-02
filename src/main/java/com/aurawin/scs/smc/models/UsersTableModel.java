package com.aurawin.scs.smc.models;

import com.aurawin.scs.stored.domain.user.Account;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UsersTableModel extends AbstractTableModel {
    public static ArrayList<Account> Accounts = new ArrayList<>();


    private String[] columnHeadings = {"UserId","Username" ,"First Name", "Last Name", "Phone", "Lock Count", "Quota", "Consumption"};
    @Override
    public int getRowCount() {
        return Accounts.size();
    }
    @Override
    public int getColumnCount() {
        return columnHeadings.length;
    }
    @Override
    public String getColumnName(int col) {
        return columnHeadings[col];
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    @Override
    public Object getValueAt(int row, int col) {
        Account acct = (Account) Accounts.get(row);
        switch (col) {
            case 0:
                return String.valueOf(acct.getId());
            case 1:
                return acct.getName();
            case 2:
                return acct.Me.getFirstName();
            case 3:
                return acct.Me.getFamilyName();
            case 4:
                String ps = acct.Me.getPhones();
                String [] pa = (ps==null) ? null :  ps.split(",");
                return (pa==null) ? null : (pa.length>0) ? pa[0] : null;
            case 5:
                return String.valueOf(acct.getLockCount());
            case 6:
                return String.valueOf(acct.getQuota());
            case 7:
                return String.valueOf(acct.getConsumption());
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void addAccont(Account acct) {
        Accounts.add(acct);
        fireTableDataChanged();
    }

    public void removeAccount(Account fs) {
        Accounts.remove(fs);
        fireTableDataChanged();
    }

    public void setAccountList(List l) {
        Accounts.clear();
        Accounts.addAll(l);
        fireTableDataChanged();
    }

    public void Clear(){
        Accounts.clear();
        fireTableDataChanged();
    }

}