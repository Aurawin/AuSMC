package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.smc.Package;
import com.aurawin.scs.smc.controllers.Domains;
import com.aurawin.scs.smc.models.CertsTableModel;
import com.aurawin.scs.smc.models.UsersTableModel;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;

public class viewDomain{
    public JComboBox cbDomains;
    private JTextField txtFriendlyName;
    private JLabel lblDomain;
    private JButton btnNewDomain;
    private JTabbedPane tpMain;
    private JCheckBox cbEnabled;
    private JSlider sldScale;

    private JTree tvClusters;
    private JPanel tabSecurity;
    private JTable tblCertificates;
    private JPanel pnlSecurityTop;
    private JButton btnCertIcon;
    private JLabel lblSecurityAllocated;
    private JButton btnCertRefresh;
    private JButton btnCertCreate;
    private JButton btnCertAssign;
    private JButton btnCertViewRequest;
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
    private JList listKeywords;
    private JPanel pnlKeywordClient;
    private JTextArea txtKeywordValue;
    private JScrollPane spTextKeywordValue;
    private JPanel tabAccounts;
    private JButton btnAccountAdd;
    private JButton btnAccountEdit;
    private JTextField txtAccountSearch;
    private JButton btnRefreshDomains;
    private JTable tblUserAccounts;
    private JTextField txtDomainNew;
    private JPanel pnlTop;
    private JButton btnCurrent;
    private JButton btnClustering;
    private JButton btnSettings;
    private JButton btnSecurity;
    private JLabel lblTitle;
    private JButton btnChange;
    private JButton btnDomain;
    private JButton btnAccountDelete;
    private JButton btnAccountFind;
    private JButton btnAccountUnlock;
    private JButton btnDomainSave;
    private JButton btnCertRemove;
    private JPanel tabCertRequest;
    private JButton cancelButton;
    private JButton generateButton;
    private JTextField txtCertReqCountry;
    private JTextField txtCertReqOrganization;
    private JTextField txtCertReqOrgUnit;
    private JTextField txtCertReqEmail;
    private JTextField txtCertReqStreet;
    private JTextField txtCertReqLocality;
    private JTextField txtCertReqState;
    private JTextField txtCertReqPostal;

    public static UsersTableModel usersTableModel;
    public static CertsTableModel certsTableModel;


    private DefaultTableCellRenderer usersTableRenderer;
    private DefaultTableCellRenderer certsTableRenderer;



    public viewDomain() {

        usersTableRenderer = new DefaultTableCellRenderer();
        certsTableRenderer = new DefaultTableCellRenderer();


        usersTableModel = new UsersTableModel();
        certsTableModel = new CertsTableModel();



        JTableHelper.setColumnAlignment(tblUserAccounts,usersTableRenderer,JLabel.CENTER);
        JTableHelper.setColumnAlignment(tblCertificates,certsTableRenderer,JLabel.CENTER);


        tblUserAccounts.setModel(usersTableModel);
        tblUserAccounts.setRowSelectionAllowed(true);

        tblCertificates.setModel(certsTableModel);
        tblCertificates.setRowSelectionAllowed(true);
        tblCertificates.setRowSelectionAllowed(true);
        tblCertificates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        tpMain.remove(tabCertRequest);

        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,0,75);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,1,75);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,2,120);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,3,120);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,4,120);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,5,50);
        JTableHelper.setColumnWidth(tblUserAccounts,usersTableRenderer,6,200);

        JTableHelper.setColumnWidth(tblCertificates,usersTableRenderer,0,75);
        JTableHelper.setColumnWidth(tblCertificates,usersTableRenderer,1,100);
        JTableHelper.setColumnWidth(tblCertificates,usersTableRenderer,2,100);
        JTableHelper.setColumnWidth(tblCertificates,usersTableRenderer,3,100);
        JTableHelper.setColumnWidth(tblCertificates,usersTableRenderer,4,50);



        btnKeywordAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        btnNewDomain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Domain d = new Domain();
                d.setName(txtDomainNew.getText());

                if (Entities.Save(d, CascadeOn)) {
                    resetView();
                } else{

                }

            }
        });

        txtCertReqCountry.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (txtCertReqCountry.getText().length() == 2) {
                    e.consume();
                }
            }
        });


        txtFriendlyName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sName = txtFriendlyName.getText();
                boolean bEn = ((sName!=null) && sName.length()>0);

                btnDomainSave.setEnabled(bEn);


            }
        });
        btnRefreshDomains.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetCertsView();
                Domain d = (Domain) cbDomains.getSelectedItem();
                Domains.refresh();
                Domains.loadView(d);
            }
        });
        cbDomains.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Domain d = (Domain) cbDomains.getSelectedItem();
                if (d!=null) {
                    d = Entities.Lookup(Domain.class, d.getName());
                    if (d != null) {
                        Domains.loadView(d);
                    } else {
                        Controller.dialogView.showDialog(
                                DialogMode.dmError,
                                Controller.Lang.DBMS.getString("dbms.domain.notfound.title"),
                                Controller.Lang.DBMS.getString("dbms.domain.notfound.message").replace("$domain", cbDomains.getSelectedItem().toString())
                        );
                    }
                }
            }
        });
        btnClustering.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapClusteringView();
            }
        });



        btnDomainSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Domains.Domain!=null){
                    Domains.saveDomain();

                }
            }
        });

        btnCertRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Domain d = (Domain) cbDomains.getSelectedItem();
                certsTableModel.loadCertificates(d);
            }
        });
        btnCertAssign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Domain d = (Domain) cbDomains.getSelectedItem();
                Certificate c = certsTableModel.getCertificate(tblCertificates.getSelectedRow());
                d.setCertId(c.getId());
                certsTableModel.certChanged(c);
                certsViewChanged();
                Entities.Update(d,CascadeOff);
            }
        });
        btnCertCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tpMain.add(tabCertRequest);
                int idx = tpMain.indexOfComponent(tabCertRequest);

                tpMain.setTitleAt(idx,Controller.Lang.Domain.getString("label.request.tab.title"));

                tpMain.setSelectedIndex(idx);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tpMain.remove(tabCertRequest);
                int idx = tpMain.indexOfComponent(tabSecurity);
                tpMain.setSelectedIndex(idx);
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Certificate c = Certificate.createRequestCertRequest(
                            Domains.Domain.getName(),
                            txtCertReqOrgUnit.getText(),
                            txtCertReqOrganization.getText(),
                            txtCertReqStreet.getText(),
                            txtCertReqLocality.getText(),
                            txtCertReqState.getText(),
                            txtCertReqPostal.getText(),
                            txtCertReqCountry.getText(),
                            txtCertReqEmail.getText()
                    );
                    c.DomainId=Domains.Domain.getId();
                    Entities.Save(c,CascadeOff);
                    certsTableModel.addCert(c);
                    tpMain.remove(tabCertRequest);
                    int idx = tpMain.indexOfComponent(tabSecurity);
                    tpMain.setSelectedIndex(idx);
                } catch (Exception ex){
                    Controller.dialogView.showDialog(
                            DialogMode.dmError,
                            Controller.Lang.Dialog.getString("title.domain.cert.exception"),
                            Controller.Lang.Dialog.getString("message.domain.cert.exception").replace("$exception",ex.getMessage())
                    );
                }
            }
        });
    }
    public void certsViewChanged() {
        Domain d = (Domain) cbDomains.getSelectedItem();
        Certificate c = certsTableModel.getCertificate(tblCertificates.getSelectedRow());
        btnCertRemove.setEnabled(c != null);
        btnCertAssign.setEnabled((c != null) && (d.getCertId() == c.Id));
        btnCertViewRequest.setEnabled((c != null) && (!c.TextRequest.isEmpty()));
        btnCertCreate.setEnabled(true);
        String ico = (d.getCertId() != 0) ? "icons/lock_open.png" : "icons/lock_closed.png";
        btnCertIcon.setIcon(new ImageIcon(ico));
    }
    public void resetCertsView(){
        certsTableModel.Clear();

        btnCertRemove.setEnabled(false);
        btnCertAssign.setEnabled(false);
        btnCertViewRequest.setEnabled(false);
        btnCertCreate.setEnabled(true);
        String ico = "icons/lock_open.png" ;
        btnCertIcon.setIcon(new ImageIcon(ico));
    }
    public void refreshView(){
        cbDomains.removeAllItems();
        for (Domain d : Domains.List){
            Controller.domainView.cbDomains.addItem(d);
        }
        Domains.setDomain((Domain) cbDomains.getSelectedItem());
        if (Domains.Domain!=null) {
            txtFriendlyName.setText(Domains.Domain.getFriendlyName());
        }
        // todo
    }
    public void saveView(){
        Domains.Domain.setFriendlyName(txtFriendlyName.getText());
    }
    public void resetView(){
        cbDomains.removeAllItems();
        for (Domain d : Domains.List){
            Controller.domainView.cbDomains.addItem(d);
        }

        txtDomainNew.setText("");
        txtFriendlyName.setText("");


        certsTableModel.Clear();
        usersTableModel.Clear();


        cbEnabled.setSelected(false);
        sldScale.setValue(0);

        lblSecurityAllocated.setText("");
        btnCertIcon.setIcon(new ImageIcon(Package.class.getResource("/icons/lock_open.png")));

        txtKeywordValue.setText("");
        txtKeywordName.setText("");
        txtAccountSearch.setText("");
    }
    public void loadView(Domain d){
        txtFriendlyName.setText(d.getFriendlyName());
        certsTableModel.loadCertificates(d);
        certsViewChanged();
    }
}
