package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.scs.stored.cloud.service.Identify;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class ClusterTableModel extends AbstractTableModel {

    public static com.aurawin.scs.stored.cloud.Group Cluster;
    public static ArrayList<Group> Clusters = new ArrayList<>();

    private String[] columnHeadings = {Controller.Lang.Clustering.getString("label.clustering.clusters")};
    @Override
    public int getRowCount() {
        return Clusters.size();
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
        Group g = (Group) Clusters.get(row);
        switch (col) {
            case 0:
                return g;
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Group getCluster(int row){
        return Clusters.get(row);
    }
    public void addCluster(Group g) {
        Clusters.add(g);
        fireTableDataChanged();
    }

    public void deleteCluster(Group g) {
        Clusters.remove(g);
        fireTableDataChanged();
    }

    public void setClusterList(List l) {
        Clusters.clear();
        Clusters.addAll(l);
        fireTableDataChanged();
    }

    public void Clear(){
        Clusters.clear();
        fireTableDataChanged();
    }


    public void ClusterChanged(Group g){
        Entities.Update(g,CascadeOff);
    }

    public void refreshView(){
        Clusters=Entities.Cloud.Group.listAll();
        fireTableDataChanged();
    }

}