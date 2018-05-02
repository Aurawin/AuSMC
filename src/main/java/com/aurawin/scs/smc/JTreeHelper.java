package com.aurawin.scs.smc;

import javax.swing.*;

public class JTreeHelper {

    public static void expandAllNodes(JTree tree, int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            JTreeHelper.expandAllNodes(tree,rowCount, tree.getRowCount());
        }
    }

}
