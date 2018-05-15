package com.aurawin.scs.smc.models;

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
import static com.aurawin.core.stored.entities.Entities.*;

public class SecurityBlacklistTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static UniqueId Namespace;
    public static ArrayList<Filter> Servers = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Security.getString("label.security.id"),
            Controller.Lang.Security.getString("label.security.counter"),
            Controller.Lang.Security.getString("label.security.server")
    };

    public SecurityBlacklistTableModel(JTable owner) {
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
        return Servers.size();
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
        Filter i = Servers.get(row);
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
        return Servers.get(row);
    }

    public void addItem(Filter i) {
        Servers.add(i);
        Entities.Save(i,CascadeOn);
        fireTableDataChanged();
    }


    public void removeItem(Filter i) {
        Servers.remove(i);
        Entities.Delete(i,CascadeOn,UseNewTransaction);
        fireTableDataChanged();
    }

    public void updateItem(Filter i){
        Entities.Update(i,CascadeOff);
        Owner.repaint();
    }

    public void refreshView(){
        Servers=Entities.Security.Filter.listAll(Identify(com.aurawin.scs.stored.security.filters.BlackList.class));
        fireTableDataChanged();
    }
    public Filter Lookup(String criteria){
        return Servers.stream()
                .filter(f->f.getValue().equals(criteria))
                .findFirst()
                .orElse(null);
    }

}
