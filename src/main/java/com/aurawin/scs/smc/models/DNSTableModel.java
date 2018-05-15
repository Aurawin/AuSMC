package com.aurawin.scs.smc.models;

import com.aurawin.core.rsr.IpHelper;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.stored.DNS;
import com.aurawin.scs.stored.Entities;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseNewTransaction;

public class DNSTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;

    public static ArrayList<DNS> Servers = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Settings.getString("label.settings.id"),
            Controller.Lang.Settings.getString("label.settings.host")
    };

    public DNSTableModel(JTable owner) {
        TableRenderer = new DefaultTableCellRenderer();
        owner.setModel(this);
        owner.setRowSelectionAllowed(true);
        owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        owner.getTableHeader().setReorderingAllowed(false);

        JTableHelper.setColumnWidth(owner, TableRenderer, 0, 75);

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
        DNS dns = Servers.get(row);
        switch (col) {
            case 0:
                return dns.getId();
            case 1:
                return IpHelper.fromLong(dns.getHost());
            default:
                return null;
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public DNS getDNS(int row) {
        return Servers.get(row);
    }

    public void hostDeleted(DNS d) {
        if (d!=null){
            Servers.remove(d);
            Entities.Delete(d,CascadeOn,UseNewTransaction);
            fireTableDataChanged();
        }
    }

    public void hostCreated(DNS d){
        if (d!=null){
            Entities.Save(d,CascadeOn);
            Servers.add(d);
            fireTableDataChanged();
        }
    }
    public void hostUpdated(DNS d){
        if (d!=null) {
            Entities.Update(d,CascadeOn );
            Owner.repaint();
            //fireTableDataChanged();
        }
    }

    public void refreshHosts(){
        Servers = Entities.Settings.DNS.listAll();
        fireTableDataChanged();
    }

}
