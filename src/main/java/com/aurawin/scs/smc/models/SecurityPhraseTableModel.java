package com.aurawin.scs.smc.models;

import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.security.Filter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

import static com.aurawin.core.solution.Namespace.Entities.Identify;
import static com.aurawin.core.solution.Namespace.Entities.getUniqueId;
import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseNewTransaction;

public class SecurityPhraseTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static UniqueId Namespace;
    public static ArrayList<Filter> Master = new ArrayList<>();
    public static ArrayList<Filter> Search= new ArrayList<>();
    public static ArrayList<Filter> Current;


    private String[] columnHeadings = {
            Controller.Lang.Security.getString("label.security.id"),
            Controller.Lang.Security.getString("label.security.counter"),
            Controller.Lang.Security.getString("label.security.phrase")
    };

    public SecurityPhraseTableModel(JTable owner) {
        TableRenderer=new DefaultTableCellRenderer();
        owner.setModel(this);
        owner.setRowSelectionAllowed(true);
        owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        owner.getTableHeader().setReorderingAllowed(false);

        JTableHelper.setColumnWidth(owner,TableRenderer,0,75);
        JTableHelper.setColumnWidth(owner,TableRenderer,1,75);
        Owner = owner;

    }

    @Override
    public int getRowCount() {
        return Current.size();
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
        Filter i = Current.get(row);
        switch (col) {
            case 0:
                return i.getId();
            case 1:
                return i.getCounter();
            case 2:
                return i.getValue();
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Filter getItem(int row){
        return Current.get(row);
    }

    public void addItem(Filter i) {
        Master.add(i);
        if (Current.indexOf(i)==-1) Current.add(i);
        Entities.Save(i,CascadeOn);
        fireTableDataChanged();
    }


    public void removeItem(Filter i) {
        Master.remove(i);
        Current.remove(i);
        Entities.Delete(i,CascadeOn,UseNewTransaction);
        fireTableDataChanged();
    }

    public void updateItem(Filter i){
        Entities.Update(i,CascadeOff);
        Owner.repaint();
    }

    public void refreshView(){
        Master=Entities.Security.Filter.listAll(Identify(com.aurawin.scs.stored.security.filters.Phrase.class));
        Current=Master;
        fireTableDataChanged();
    }
    public Filter Lookup(String criteria){
        return Master.stream()
                .filter(f->f.getValue().equals(criteria))
                .findFirst()
                .orElse(null);
    }
    public void searchItems(String criteria){
        Search.clear();
        if (criteria.length()==0) {
            Current=Master;
        }else {
            for (Filter f : Master) {
                if (f.getValue().indexOf(criteria) > -1)
                    Search.add(f);
            }
            Current = Search;
        }
        fireTableDataChanged();
    }

}
