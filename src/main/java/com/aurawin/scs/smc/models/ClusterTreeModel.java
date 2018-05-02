package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.JTreeHelper;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Resource;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EventListener;

public class ClusterTreeModel implements TreeModel,TreeModelListener {
    private JTree Owner;
    private ArrayList<Group>Clusters;
    private String Root;
    private TreePath pathSelected;

    public void refreshView() {
        Clusters= Entities.Cloud.Group.listAll();
        reload();
    }

    public ClusterTreeModel(JTree owner) {

        Owner = owner;
        //Root = (DefaultMutableTreeNode) owner.getModel().getRoot();
        Owner.setRootVisible(false);
        Root = new String("Root");

        owner.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                pathSelected=e.getNewLeadSelectionPath();
                if (pathSelected!=null) {
                    Object src = pathSelected.getLastPathComponent();

                    if (src instanceof Group) {
                        Controller.clusteringView.selectGroup((Group) src, true);
                    } else if (src instanceof Resource) {
                        Controller.clusteringView.selectResource((Resource) src, true);
                    } else if (src instanceof Node) {
                        Controller.clusteringView.selectNode((Node) src, true);
                    }
                }
            }
        });
        owner.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    }



    // The model knows how to return the root object of the tree
    public Object getRoot() {
        return Root;
    }

    // Tell JTree whether an object in the tree is a leaf
    public boolean isLeaf(Object node) {
        return  (node instanceof Node);
    }

    // Tell JTree how many children a node has
    public int getChildCount(Object parent) {
        JTreeHelper.expandAllNodes(Owner,0,Owner.getRowCount());
        if (parent instanceof String) {
            return (Clusters!=null) ? Clusters.size():0;
        } else if (parent instanceof Group){
            return ((Group) parent).Resources.size();
        } else if (parent instanceof Resource){
            return ((Resource) parent).Nodes.size();
        } else {
            return 0;
        }

    }

    // Fetch any numbered child of a node for the JTree.
    // Our model returns File objects for all nodes in the tree.  The
    // JTree displays these by calling the File.toString() method.
    public Object getChild(Object parent, int index) {
        if (parent instanceof String){
            return Clusters.get(index);
        } else if (parent instanceof Group){
            return ((Group)parent).Resources.get(index);
        } else if (parent instanceof Resource) {
            return ((Resource) parent).Nodes.get(index);
        } else {
            return null;
        }
    }

    // Figure out a child's position in its parent node.
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof String){
            return Clusters.indexOf(child);
        } else if (parent instanceof Group){
            return ((Group)parent).Resources.indexOf(child);
        } else if (parent instanceof Resource) {
            return ((Resource) parent).Nodes.indexOf(child);
        } else {
            return -1;
        }
    }

    public void reload(){
        treeStructureChanged(new TreeModelEvent(Owner, new Object[] {getRoot()}));
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
        //todo
    }

    public void removeTreeModelListener(TreeModelListener l)
    {

    }

    public void addTreeModelListener(TreeModelListener l)
    {

    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
        Owner.expandPath(e.getTreePath());
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
        JTreeHelper.expandAllNodes(Owner,0,Owner.getRowCount());
        Owner.repaint();
    }
    @Override
    public void treeNodesChanged(TreeModelEvent e) {

    }
}
