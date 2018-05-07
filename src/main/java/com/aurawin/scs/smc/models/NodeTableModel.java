package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Resource;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class NodeTableModel extends AbstractTableModel {
    protected JTable Owner;
    public static Resource Resource;

    public static ArrayList<Node> Nodes = new ArrayList<>();

    private String[] columnHeadings = {
            Controller.Lang.Clustering.getString("label.clustering.id"),
            Controller.Lang.Clustering.getString("label.clustering.nodes")
    };

    public NodeTableModel(JTable owner) {
        owner.getTableHeader().setReorderingAllowed(false);
        Owner = owner;
    }

    @Override
    public int getRowCount() {
        return Nodes.size();
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
        Node n = (Node) Nodes.get(row);
        switch (col) {
            case 0:
                return n.getId();
            case 1:
                return n.getName();
            default:
                return null;
        }
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Node getNode(int row){
        return Nodes.get(row);
    }
    public void addNode(Node n) {
        Nodes.add(n);
        fireTableDataChanged();
    }

    public void deleteNode(Node n) {
        Nodes.remove(n);
        fireTableDataChanged();
    }

    public void Clear(){
        Nodes.clear();
        fireTableDataChanged();
    }


    public void NodeChanged(Node n){
        Owner.repaint();
        Entities.Update(n,CascadeOff);
    }

    public void refreshView(Resource r){
        if ( (Resource==null) || (Resource!=null) && Resource.getId()!=r.getId()) {
            Resource = r;
            Nodes = Entities.Cloud.Node.listAll(r);
            fireTableDataChanged();
        }
    }

}