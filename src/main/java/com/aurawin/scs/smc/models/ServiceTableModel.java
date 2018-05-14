package com.aurawin.scs.smc.models;

import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.scs.stored.cloud.service.Identify;
import com.aurawin.scs.stored.domain.user.Account;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class ServiceTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static ArrayList<Service> Services = new ArrayList<>();

    private String[] columnHeadings = {"Id","Enabled" ,"Scale", "Service", "Description"};


    public ServiceTableModel(JTable owner) {
        Owner = owner;
        Owner.setModel(this);
        Owner.setRowSelectionAllowed(true);
        Owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRenderer = new DefaultTableCellRenderer();
        owner.getTableHeader().setReorderingAllowed(false);

        JTableHelper.setColumnWidth(owner,TableRenderer,0,75);
        JTableHelper.setColumnWidth(owner,TableRenderer,1,75);
        JTableHelper.setColumnWidth(owner,TableRenderer,2,55);
        JTableHelper.setColumnWidth(owner,TableRenderer,3,140);


    }

    @Override
    public int getRowCount() {
        return Services.size();
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
        Service svc = (Service) Services.get(row);
        switch (col) {
            case 0:
                return String.valueOf(svc.getId());
            case 1:
                return ServiceModel.Enabled.get(svc.getEnabled());
            case 2:
                return String.valueOf(svc.getScaleStart());
            case 3:
                return svc.getKind().getValue();

            case 4:
                return svc.getKind().getDescription();
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Service getService(int row){
        return Services.get(row);
    }
    public void addService(Service svc) {
        Services.add(svc);
        fireTableDataChanged();
    }

    public void removeService(Service svc) {
        Services.remove(svc);
        fireTableDataChanged();
    }

    public void Clear(){
        Services.clear();
        fireTableDataChanged();
    }

    public void refreshView(Node node){
        if (node!=null) {
            Services = Identify.Force(node);
        }
        fireTableDataChanged();
    }
    public void serviceChanged(Service svc){

        Entities.Update(svc,CascadeOff);
    }



}