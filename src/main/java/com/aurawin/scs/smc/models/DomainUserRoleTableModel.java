package com.aurawin.scs.smc.models;

import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.user.Role;
import com.aurawin.scs.stored.security.Filter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

import static com.aurawin.core.solution.Namespace.Entities.Identify;
import static com.aurawin.core.stored.entities.Entities.*;

public class DomainUserRoleTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static UniqueId Namespace;
    public static ArrayList<Role> Items = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Domain.getString("label.domain.id"),
            Controller.Lang.Domain.getString("label.domain.role.name"),
            Controller.Lang.Domain.getString("label.domain.role.description")
    };

    public DomainUserRoleTableModel(JTable owner) {
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
        return Items.size();
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
        Role i = Items.get(row);
        switch (col) {
            case 0:
                return i.getId();
            case 1:
                return i.Name;
            case 2:
                return i.Description;
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Role getItem(int row){
        return Items.get(row);
    }

    public void addItem(Role i) {
        Items.add(i);
        Entities.Save(i,CascadeOn);
        fireTableDataChanged();
    }


    public void removeItem(Filter i) {
        Items.remove(i);
        Entities.Delete(i,CascadeOn,UseNewTransaction);
        fireTableDataChanged();
    }

    public void updateItem(Filter i){
        Entities.Update(i,CascadeOff);
        Owner.repaint();
    }

    public void refreshView(){
        //Items=Entities.Security.Filter.listAll(Identify(com.aurawin.scs.stored.security.filters.BlackList.class));
        fireTableDataChanged();
    }
    public Role Lookup(String criteria){
        return Items.stream()
                .filter(f->f.Name.equals(criteria))
                .findFirst()
                .orElse(null);
    }

}
