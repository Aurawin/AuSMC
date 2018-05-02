package com.aurawin.scs.smc;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JTableHelper {

    public static void setColumnWidth(JTable tbl, DefaultTableCellRenderer Renderer, int col, int width){
        tbl.getColumnModel().getColumn(col).setPreferredWidth(width);
        tbl.getColumnModel().getColumn(col).setMaxWidth(width);
        tbl.getColumnModel().getColumn(col).setMinWidth(width);
        tbl.getColumnModel().getColumn(col).setCellRenderer(Renderer);
    }
    public static void setColumnAlignment(JTable tbl,DefaultTableCellRenderer Renderer, int alignment){
        DefaultTableCellRenderer tcr = (DefaultTableCellRenderer) tbl.getTableHeader().getDefaultRenderer();
        tcr.setHorizontalAlignment(alignment);
        Renderer.setHorizontalAlignment(alignment);
    }
}
