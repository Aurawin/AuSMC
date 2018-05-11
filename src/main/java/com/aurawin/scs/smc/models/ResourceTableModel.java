package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Resource;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class ResourceTableModel extends AbstractTableModel {
    protected JTable Owner;
    private DefaultTableCellRenderer TableRenderer;
    public static ArrayList<Resource> Resources = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Clustering.getString("label.clustering.id"),
            Controller.Lang.Clustering.getString("label.clustering.resources")
    };

    public ResourceTableModel(JTable owner) {
        Owner = owner;
        Owner.setModel(this);
        Owner.setRowSelectionAllowed(true);
        Owner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRenderer= new DefaultTableCellRenderer();
        owner.getTableHeader().setReorderingAllowed(false);
        JTableHelper.setColumnWidth(owner,TableRenderer,0,75);

    }

    @Override
    public int getRowCount() {
        return Resources.size();
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
        Resource r = (Resource) Resources.get(row);
        switch (col) {
            case 0:
                return r.getId();
            case 1:
                return r.getName();
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Resource getResource(int row){
        return Resources.get(row);
    }
    public void addResource(Resource r) {
        Resources.add(r);
        fireTableDataChanged();
    }

    public void deleteResource(Resource r) {
        Resources.remove(r);
        fireTableDataChanged();
    }

    public void setResourcesList(List l) {
        Resources.clear();
        Resources.addAll(l);
        fireTableDataChanged();
    }

    public void Clear(){
        Resources.clear();
        fireTableDataChanged();
    }


    public void ResourceChanged(Resource r){
        Owner.repaint();
        Entities.Update(r,CascadeOff);
    }

    public void refreshView(Group cluster){
        if (
                (Controller.clusteringView.Cluster==null) ||
                (Controller.clusteringView.Cluster!=null) &&
                (Controller.clusteringView.Cluster.getId()!=cluster.getId())
        ) {
            Resources = Entities.Cloud.Resource.listAll(cluster);
            fireTableDataChanged();
        }
    }

}