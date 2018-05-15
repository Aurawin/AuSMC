package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.stored.ContentType;
import com.aurawin.scs.stored.Entities;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;

public class ContentTypeTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static ArrayList<ContentType> ContentTypes = new ArrayList<>();
    private String[] columnHeadings = {
            Controller.Lang.Settings.getString("label.settings.id"),
            Controller.Lang.Settings.getString("label.settings.content.types.type"),
            Controller.Lang.Settings.getString("label.settings.content.types.extension")
    };

    public ContentTypeTableModel(JTable owner) {
        TableRenderer=new DefaultTableCellRenderer();
        owner.setModel(this);
        owner.setRowSelectionAllowed(true);
        owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        owner.getTableHeader().setReorderingAllowed(false);

        JTableHelper.setColumnWidth(owner,TableRenderer,0,75);
        JTableHelper.setColumnWidth(owner,TableRenderer,1,200);
        Owner = owner;

    }

    @Override
    public int getRowCount() {
        return ContentTypes.size();
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
        ContentType ct = ContentTypes.get(row);
        switch (col) {
            case 0:
                return ct.getId();
            case 1:
                return ct.getMajor()+"/"+ct.getMinor();
            case 2:
                return ct.getExt();
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public ContentType getContentType(int row){
        return ContentTypes.get(row);
    }

    public void addContentType(ContentType ct) {
        ContentTypes.add(ct);
        Entities.Save(ct,CascadeOn);
        fireTableDataChanged();
    }

    public void removeContentType(ContentType ct) {
        ContentTypes.remove(ct);
        fireTableDataChanged();
    }

    public void ContentTypeChanged(ContentType ct){
        Entities.Update(ct,CascadeOff);
    }

    public void refreshView(){
        ContentTypes=Entities.Settings.ContentType.listAll();
        fireTableDataChanged();
    }

}