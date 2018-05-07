package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Resource;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class ResourceTableModel extends AbstractTableModel {
    protected JTable Owner;
    public static Group Cluster;

    public static ArrayList<Resource> Resources = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Clustering.getString("label.clustering.id"),
            Controller.Lang.Clustering.getString("label.clustering.resources")
    };

    public ResourceTableModel(JTable owner) {
        owner.getTableHeader().setReorderingAllowed(false);
        Owner = owner;
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
        if ( (Cluster==null) || (Cluster!=null) && Cluster.getId()!=cluster.getId()) {
            Cluster = cluster;
            Resources = Entities.Cloud.Resource.listAll(Cluster);
            fireTableDataChanged();
        }
    }

}