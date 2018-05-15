package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.models.SecurityBlacklistTableModel;
import com.aurawin.scs.smc.models.SecurityPhraseTableModel;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.security.Filter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class viewSecurity {
    public JPanel mainPanel;
    private JPanel pnlTop;
    private JButton btnCurrent;
    private JButton btnClustering;
    private JButton btnSettings;
    private JButton btnSecurity;
    private JButton btnChange;
    private JButton btnDomain;
    private JLabel lblTitle;
    private JLabel lblGroup;
    private JLabel lblResource;
    private JLabel lblNode;
    private JLabel lblMount;
    private JTabbedPane tpSecurity;
    private JPanel tabContentFilters;
    private JButton btnSecurityFilterDelete;
    private JTextField txtPhrase;
    private JButton btnSecurityFilterAdd;
    private JButton btnSecurityFilterSave;
    private JTable tblSecurityPhrases;
    private JPanel tabBlackListFilters;
    private JPanel pnlKeywordTools;
    private JLabel lblKeywordName;
    private JButton btnBlackListDelete;
    private JTextField txtBlackListServer;
    private JButton btnBlackListAdd;
    private JButton btnBlackListSave;
    private JTable tblBlackListServers;
    private JButton btnSecurityFilterSearch;

    private boolean navigationExpanded;
    private SecurityPhraseTableModel pTableModel;
    private SecurityBlacklistTableModel bTableModel;
    public viewSecurity() {
        pTableModel = new SecurityPhraseTableModel(tblSecurityPhrases);
        bTableModel = new SecurityBlacklistTableModel(tblBlackListServers);

        navigationExpanded=true;
        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapSettingsView();
            }
        });
        btnDomain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapDomainView();
            }
        });
        btnClustering.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapClusteringView();
            }
        });
        btnChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigationExpanded = !navigationExpanded;
                btnClustering.setVisible(navigationExpanded);
                btnSecurity.setVisible(navigationExpanded);
                btnDomain.setVisible(navigationExpanded);
                btnSettings.setVisible(navigationExpanded);
                try {
                    btnChange.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream((navigationExpanded) ? "icons/arrowhead_left.png" : "icons/arrowhead_right.png"))));
                } catch (Exception ex){

                }
            }
        });
        btnSecurityFilterDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblSecurityPhrases.getSelectedRow();
                if (idx!=-1) {
                    Filter f = pTableModel.getItem(idx);
                    pTableModel.removeItem(f);
                }
            }
        });
        btnSecurityFilterAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Filter i = new Filter(Namespace.Entities.getUniqueId(com.aurawin.scs.stored.security.filters.Phrase.class));
                i.setValue(txtPhrase.getText());
                pTableModel.addItem(i);
            }
        });
        btnSecurityFilterSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblSecurityPhrases.getSelectedRow();
                if (idx!=-1) {
                    Filter i = pTableModel.getItem(idx);
                    i.setValue(txtPhrase.getText());
                    pTableModel.updateItem(i);
                }

            }
        });
        btnSecurityFilterSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pTableModel.searchItems(txtPhrase.getText());
            }
        });

        txtPhrase.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sP = txtPhrase.getText();
                boolean enP = ( (sP!=null) );

                boolean selected = tblSecurityPhrases.getSelectedRow()!=-1;
                if (enP) {
                    boolean enNotExist = pTableModel.Lookup(sP)==null;
                    btnSecurityFilterAdd.setEnabled(enNotExist);
                    btnSecurityFilterSave.setEnabled(enNotExist);
                    btnSecurityFilterDelete.setEnabled(selected);
                } else {
                    btnSecurityFilterAdd.setEnabled(false);
                    btnSecurityFilterSave.setEnabled(false);
                    btnSecurityFilterDelete.setEnabled(false);
                }

            }
        });
        txtBlackListServer.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sP = txtBlackListServer.getText();
                boolean enP = ( (sP!=null) );

                boolean selected = tblBlackListServers.getSelectedRow()!=-1;
                if (enP) {
                    boolean enNotExist = bTableModel.Lookup(sP)==null;
                    btnBlackListAdd.setEnabled(enNotExist);
                    btnBlackListSave.setEnabled(enNotExist);
                    btnBlackListDelete.setEnabled(selected);
                } else {
                    btnBlackListAdd.setEnabled(false);
                    btnBlackListSave.setEnabled(false);
                    btnBlackListDelete.setEnabled(false);
                }

            }
        });
        btnBlackListDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblBlackListServers.getSelectedRow();
                if (idx!=-1) {
                    Filter f = bTableModel.getItem(idx);
                    bTableModel.removeItem(f);
                }
            }
        });
        btnBlackListAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Filter i = new Filter(Namespace.Entities.getUniqueId(com.aurawin.scs.stored.security.filters.BlackList.class));
                i.setValue(txtBlackListServer.getText());
                bTableModel.addItem(i);
            }
        });
        btnBlackListSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblBlackListServers.getSelectedRow();
                if (idx!=-1) {
                    Filter i = bTableModel.getItem(idx);
                    i.setValue(txtBlackListServer.getText());
                    bTableModel.updateItem(i);
                }
            }
        });
        tblSecurityPhrases.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                   int idx = tblSecurityPhrases.getSelectedRow();
                   if (idx!=-1) {
                       Filter i = pTableModel.getItem(idx);
                       txtPhrase.setText(i.getValue());
                   }
                }

            }
        });

        tblBlackListServers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = tblBlackListServers.getSelectedRow();
                    if (idx!=-1) {
                        Filter i = bTableModel.getItem(idx);
                        txtBlackListServer.setText(i.getValue());
                    }
                }

            }
        });
    }
    public void loadViews(){
        loadContentFilters();
        loadDNSFilters();
    }
    public void loadContentFilters(){
        pTableModel.refreshView();
    }
    public void loadDNSFilters(){
        bTableModel.refreshView();
    }
}
