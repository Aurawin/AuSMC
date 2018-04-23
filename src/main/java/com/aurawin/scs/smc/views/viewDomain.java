package com.aurawin.scs.smc.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewDomain{
    private JComboBox cbDomains;
    private JTextField txtFriendlyName;
    private JLabel lblDomain;
    private JButton btnNewDomain;
    private JTabbedPane tpMain;
    private JPanel tabServices;
    private JCheckBox cbEnabled;
    private JSlider sldScale;
    private JTable table1;
    private JTree tree1;
    private JButton allocateButton;
    private JButton releaseButton;
    private JComboBox cbClusterIP;
    private JPanel tabSecurity;
    private JPanel tabClustering;
    private JTable table2;
    private JPanel pnlSecurityTop;
    private JButton btnCertIcon;
    private JLabel lblSecurityAllocated;
    private JButton btnCertRefresh;
    private JButton btnCertDelete;
    private JButton btnCertAssign;
    private JButton btnCertRequest;
    private JButton btnCertImport;
    private JButton btnLoad;
    private JComboBox cbCertLoad;
    private JPanel tabKeywords;
    private JPanel pnlKeywordTools;
    private JButton btnKeywordAdd;
    private JButton btnKeywordDelete;
    private JTextField txtKeywordName;
    private JLabel lblKeywordName;
    public JPanel mainPanel;
    private JList listKeywordKeywords;
    private JPanel pnlKeywordClient;
    private JTextArea txtKeywordValue;
    private JScrollPane spTextKeywordValue;
    private JPanel tabAccounts;

    public viewDomain() {
        btnKeywordAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
