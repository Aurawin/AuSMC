package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.List;

public class UsersTableModel extends AbstractTableModel {
    protected static JTable Owner;
    protected static Domain Domain;
    protected static Account User;
    protected DefaultTableCellRenderer tableRenderer;

    public static ArrayList<Account> Accounts = new ArrayList<>();


    private String[] columnHeadings = {"Id","Username" ,"First Name", "Last Name", "Phone", "Lock Count", "Quota", "Consumption"};


    public UsersTableModel(JTable owner) {
        Owner = owner;
        tableRenderer = new DefaultTableCellRenderer();

        JTableHelper.setColumnAlignment(owner,tableRenderer,JLabel.CENTER);

        owner.setModel(this);
        owner.getTableHeader().setReorderingAllowed(false);
        owner.setRowSelectionAllowed(true);
        owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHelper.setColumnWidth(owner,tableRenderer,0,50);
        JTableHelper.setColumnWidth(owner,tableRenderer,1,75);
        JTableHelper.setColumnWidth(owner,tableRenderer,2,100);
        JTableHelper.setColumnWidth(owner,tableRenderer,3,100);
        JTableHelper.setColumnWidth(owner,tableRenderer,4,100);
        JTableHelper.setColumnWidth(owner,tableRenderer,5,80);
        JTableHelper.setColumnWidth(owner,tableRenderer,6,120);

    }

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

        Account ua = getUser(0);
        if (ua!=null) Owner.setRowSelectionInterval(0, 0);
    }

    public void setAccountList(List l) {
        Accounts.clear();
        Accounts.addAll(l);
        fireTableDataChanged();
    }

    public void setDomain(Domain d){
        Domain = d;
        if (d==null){
            Clear();
        } else {
            Accounts=Entities.Domains.Users.listAll(d);
            fireTableDataChanged();
        }
        Domain = d;

    }
    public Account getCurrentUser(){
        return User;
    }
    public void setCurrentUser(Account ua){
        User = ua;
    }
    public Account getUser(int idx){
        if ( (idx==-1) ||idx>=Accounts.size()) {
            return null;
        } else {
            return Accounts.get(idx);
        }
    }
    public void Clear(){
        Accounts.clear();
        fireTableDataChanged();
    }

}