package com.aurawin.scs.smc.views;

import com.aurawin.core.file.OpenFileFilter;
import com.aurawin.core.file.SystemDialog;
import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.Package;
import com.aurawin.scs.smc.controllers.DialogCompletion;
import com.aurawin.scs.smc.controllers.Domains;
import com.aurawin.scs.smc.controllers.OnDialogCompletion;
import com.aurawin.scs.smc.models.CertsTableModel;
import com.aurawin.scs.smc.models.UsersTableModel;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.KeyValue;
import com.aurawin.scs.stored.domain.user.Account;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.aurawin.core.file.DialogKind.dkOpen;
import static com.aurawin.core.file.DialogMode.dmFile;
import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseNewTransaction;

public class viewDomain{
    public static boolean Loading;
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
    private JButton btnCertKeyImport;
    private JButton btnLoad;
    private JPanel tabKeywords;
    private JPanel pnlKeywordTools;
    private JButton btnKeywordAdd;
    private JButton btnKeywordDelete;
    private JTextField txtKeywordName;
    private JLabel lblKeywordName;
    public JPanel mainPanel;
    private JList lstKeywords;

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
    private JTextPane txtKeywordValue;
    private JPanel pnlKeywordValue;
    private JSplitPane spKeywords;
    private JPanel tabAccount;
    private JTabbedPane tpUser;
    private JPanel tabUserEditor;
    private JPanel tabUserCore;
    private JTextField txtUserFirstName;
    private JTextField txtUserLastName;
    private JPasswordField txtUserPassword;
    private JTextField txtUserTelephone;
    private JTextField txtUserConsumption;
    private JSpinner spUserQuota;
    private JButton btnUserSave;
    private JButton btnUserEditCancel;
    private JLabel lblResource;
    private JLabel lblNode;
    private JLabel lblMount;
    private JLabel lblGroup;
    private JButton btnKeywordSave;
    private JTable tblUserRoles;


    public static UsersTableModel usersTableModel;
    public static CertsTableModel certsTableModel;

    public SystemDialog KeyDialog;
    public SystemDialog CertDialog;
    public boolean navigationExpanded;

    public viewDomain() {
        Loading=false;
        navigationExpanded=true;
        OpenFileFilter off;
        KeyDialog = new SystemDialog(dkOpen,dmFile);
        off= new OpenFileFilter("key","Key Files (*.key)");
        KeyDialog.addChoosableFileFilter(off);
        KeyDialog.setFileFilter(off);
        KeyDialog.setDialogTitle(Controller.Lang.Domain.getString("title.dialog.cert.key"));

        CertDialog = new SystemDialog(dkOpen,dmFile);
        off = new OpenFileFilter("crt","Certificate Files (*.crt)");
        CertDialog.addChoosableFileFilter(off);
        CertDialog.setFileFilter(off);
        CertDialog.setDialogTitle(Controller.Lang.Domain.getString("title.dialog.cert"));


        usersTableModel = new UsersTableModel(tblUserAccounts);
        certsTableModel = new CertsTableModel(tblCertificates);



        tblUserAccounts.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int idx = tblUserAccounts.getSelectedRow();
                Account ua = usersTableModel.getUser(idx);
                usersTableModel.setCurrentUser(ua);
            }

        });
        tblUserAccounts.addMouseListener( new MouseInputListener() {
            public void mouseDragged(MouseEvent e){}
            public void mouseMoved(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    Account ua = usersTableModel.getUser(tblUserAccounts.getSelectedRow());
                    loadUserView(ua);
                }
            }

        });


        tblCertificates.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int idx = tblCertificates.getSelectedRow();
                btnCertRemove.setEnabled(idx!=-1);
                btnCertAssign.setEnabled(idx!=-1);
            }

        });




        tpMain.remove(tabCertRequest);
        tpMain.remove(tabAccount);



        spUserQuota.setModel((SpinnerModel) new SpinnerNumberModel(Long.valueOf(0),Long.valueOf(0),Long.valueOf(1000*1000),Long.valueOf(1000)));

        lstKeywords.setFixedCellHeight(40);

        btnKeywordAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sName = txtKeywordName.getText();
                KeyValue kv = Domains.getKeyword(sName);
                if (kv==null){
                    kv = new KeyValue(
                            Domains.Domain.getId(),
                            Namespace.Entities.Identify(KeyValue.class),
                            sName);
                    Entities.Save(kv,CascadeOff);
                    DefaultListModel<String> m = (DefaultListModel<String>)  lstKeywords.getModel();
                    m.addElement(sName);
                    Domains.Keywords.add(kv);
                    lstKeywords.setSelectedIndex(Domains.Keywords.size()-1);
                } else {
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){}
                            },
                            DialogMode.dmError,
                            Controller.Lang.Domain.getString("domain.keyword.new.exists.title").replace("$keyword", sName),
                            Controller.Lang.Domain.getString("domain.keyword.new.exists.message").replace("$keyword", sName)
                    );

                }
            }
        });


        btnNewDomain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Domain d;
                d = Entities.Lookup(Domain.class,txtDomainNew.getText());
                if (d!=null){
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){}
                            },
                            DialogMode.dmError,
                            Controller.Lang.Domain.getString("domain.new.exists.title").replace("$domain", cbDomains.getSelectedItem().toString()),
                            Controller.Lang.Domain.getString("domain.new.exists.message").replace("$domain", cbDomains.getSelectedItem().toString())
                    );
                } else {
                    d = new Domain();
                    d.setName(txtDomainNew.getText());
                    d.setFriendlyName(txtFriendlyName.getText());
                    Entities.Save(d, CascadeOn);
                    Domains.refresh();
                    Domains.loadView(d);
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

        txtKeywordName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                int idx = lstKeywords.getSelectedIndex();
                btnKeywordSave.setEnabled((idx!=-1));
            }
        });

        txtFriendlyName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                String sName = txtFriendlyName.getText();
                String sDomain = txtDomainNew.getText();

                boolean bEn = ((sName!=null) && sName.length()>0);
                bEn = (bEn && ((Domains.Domain!=null) && (sDomain.equalsIgnoreCase(Domains.Domain.getName()))));
                btnDomainSave.setEnabled(bEn);


            }
        });
        txtDomainNew.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sDomain = txtDomainNew.getText();

                boolean bEn = ((sDomain!=null) && sDomain.length()>0);
                bEn = (bEn && ((Domains.Exists(sDomain)==false)));
                btnNewDomain.setEnabled(bEn);
            }
        });
        txtKeywordName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sKeyword = txtKeywordName.getText();

                boolean bEn = ((sKeyword!=null) && sKeyword.length()>0);
                bEn = (bEn && ((Domains.keywordExists(sKeyword)==false)));
                btnKeywordAdd.setEnabled(bEn);
            }
        });

        btnRefreshDomains.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Loading=true;
                try {
                    resetKeywordView();
                    resetCertsView();
                    Domain d = (Domain) cbDomains.getSelectedItem();
                    Domains.refresh();
                    Domains.loadView(d);
                } finally{
                    Loading = false;
                }
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
                                new OnDialogCompletion(){
                                    @Override
                                    public void Complete (DialogCompletion Data){}
                                },
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
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){}
                            },
                            DialogMode.dmError,
                            Controller.Lang.Dialog.getString("title.domain.cert.exception"),
                            Controller.Lang.Dialog.getString("message.domain.cert.exception").replace("$exception",ex.getMessage())
                    );
                }
            }
        });
        btnCertKeyImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (KeyDialog.showOpenDialog(Controller.frameMain)==JFileChooser.APPROVE_OPTION){
                    MemoryStream ms = new MemoryStream();
                    try {
                        ms.LoadFromFile(KeyDialog.getSelectedFile());
                        ms.Position = 0;
                        Certificate c = new Certificate();
                        c.KeyPrivate = ms.Read();
                        c.DomainId=Domains.Domain.getId();
                        Entities.Save(c,CascadeOff);
                        certsTableModel.loadCertificates(Domains.Domain);
                    } catch (Exception ex){

                    } finally{
                        ms.close();
                    }
                }
            }
        });
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CertDialog.showOpenDialog(Controller.frameMain)==JFileChooser.APPROVE_OPTION){
                    MemoryStream ms = new MemoryStream();
                    try {
                        ms.LoadFromFile(CertDialog.getSelectedFile());
                        ms.Position = 0;
                        // todo assume chain instead of singleton certificate
                        Certificate c =certsTableModel.getCertificate(tblCertificates.getSelectedRow());
                        c.TextCert1=ms.toString();
                        c.DerCert1=Settings.Security.Certificate.decode(c.TextCert1);
                        c.KeyPublic=Settings.Security.Certificate.extractPublicKey(c.DerCert1);
                        c.ChainCount=1;
                        Entities.Update(c,CascadeOff);
                        certsTableModel.loadCertificates(Domains.Domain);
                    } catch (Exception ex){

                    } finally{
                        ms.close();
                    }
                }
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
        btnCertRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Certificate c = certsTableModel.getCertificate(tblCertificates.getSelectedRow());
                if (c!=null){
                    certsTableModel.removeCert(c);
                    Entities.Delete(c,CascadeOn,UseNewTransaction);
                }
            }
        });
        lstKeywords.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int idx = lstKeywords.getSelectedIndex();
                if (idx!=-1){
                    Loading = true;
                    try {
                        KeyValue v = Domains.Keywords.get(idx);
                        txtKeywordName.setText(v.getName());
                        txtKeywordValue.setText(v.getValue());
                        txtKeywordValue.setEnabled(true);
                        btnKeywordSave.setEnabled(true);
                        btnKeywordDelete.setEnabled(true);
                    } finally{
                        Loading =false;
                    }
                }
            }
        });
        spKeywords.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Loading) return;
                JSplitPane sp = (JSplitPane) evt.getSource();
                String propertyName = evt.getPropertyName();
                if (propertyName.equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    Controller.Configuration.setKeywordSplitterSize(sp.getDividerLocation());
                }
            }
        });
        btnAccountEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account ua = usersTableModel.getCurrentUser();
                loadUserView(ua);
            }
        });
        btnUserSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account ua = usersTableModel.getCurrentUser();
                saveUserView(ua);
            }
        });
        btnUserEditCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetUsersView();
            }
        });
        btnAccountDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account ua = usersTableModel.getCurrentUser();
                if (
                        (ua.getName().equalsIgnoreCase(com.aurawin.core.lang.Table.String(com.aurawin.scs.lang.Table.Entities.Domain.Root)))

                ) return;
                Controller.dialogView.showDialog(
                        new OnDialogCompletion(){
                            @Override
                            public void Complete (DialogCompletion Data){
                                Entities.Purge(ua,CascadeOn);
                                Entities.Delete(ua,CascadeOn,UseNewTransaction);
                                usersTableModel.removeAccount(ua);
                            }
                        },
                        DialogMode.dmError,
                        Controller.Lang.Domain.getString("domain.user.delete.title").replace("$account", ua.getName()),
                        Controller.Lang.Domain.getString("domain.user.delete.message").replace("$account", ua.getName())
                );

            }
        });
        btnAccountUnlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account ua = usersTableModel.getCurrentUser();
                ua.setLockCount(0);
                Entities.Update(ua,CascadeOff);
            }
        });
        btnAccountAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((txtAccountSearch.getText()==null) || (txtAccountSearch.getText().length()==0)) return;
                Account ua = Entities.Lookup(Account.class,Domains.Domain.getId(),txtAccountSearch.getText());
                if (ua==null){
                    ua = new Account(Domains.Domain,txtAccountSearch.getText());
                    Entities.Save(ua,CascadeOn);
                    loadUserView(ua);
                }
            }
        });
        btnKeywordSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = lstKeywords.getSelectedIndex();
                if (idx!=-1){
                    KeyValue v = Domains.Keywords.get(idx);
                    String sName = txtKeywordName.getText();
                    if (v!=null) {
                        v.setName(sName);
                        v.setValue(txtKeywordValue.getText());
                        ((DefaultListModel)lstKeywords.getModel()).setElementAt(sName,idx);
                        Entities.Update(v, CascadeOff);
                    }
                }
            }
        });
        btnKeywordDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = lstKeywords.getSelectedIndex();
                if (idx!=-1){
                    KeyValue v = Domains.Keywords.get(idx);
                    if (v!=null) {
                        Domains.Keywords.remove(v);
                        Entities.Delete(v, CascadeOn,UseNewTransaction);
                        refreshKeywords();
                    }
                }
            }
        });
        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapSettingsView();
            }
        });
        btnSecurity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapSecurityView();
            }
        });
    }
    public void keywordsViewChanged(){
        txtKeywordValue.setEnabled(false);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (KeyValue k:Domains.Keywords){
            listModel.addElement(k.getName());
        }

    }
    public void saveUserView(Account ua){
        String[] phones;
        String phone;
        String newPhone;
        phone = ua.Me.getPhones();
        newPhone = txtUserTelephone.getText();
        if ( (newPhone!=null) && (newPhone.length()>0)) {
            ArrayList<String> saPhones = new ArrayList<>();
            phones = ((phone == null) || phone.length() == 0) ? new String[0] : phone.split(",");
            for (String p : phones) {
                saPhones.add(p);
            }
            saPhones = saPhones.stream()
                    .filter(s->!s.equalsIgnoreCase(newPhone))
                    .collect(Collectors.toCollection(ArrayList::new));
            saPhones.add(0,newPhone);
            ua.Me.setPhones(String.join(",",saPhones));
        } else {
            ua.Me.setPhones("");
        }

        ua.Me.setFirstName(txtUserFirstName.getText());
        ua.Me.setFamilyName(txtUserLastName.getText());
        ua.setQuota(((Long) spUserQuota.getValue()).longValue());
        ua.setPass(String.valueOf(txtUserPassword.getPassword()));

        Entities.Update(ua,CascadeOn);

        resetUsersView();

    }
    public void usersViewChanged(){
        txtAccountSearch.setText("");
        tpMain.remove(tabAccount);
    }
    public void certsViewChanged() {
        certsTableModel.loadCertificates(Domains.Domain);
        Certificate c = certsTableModel.getCertificate(tblCertificates.getSelectedRow());
        btnCertRemove.setEnabled(c != null);
        btnCertAssign.setEnabled((c != null) && (Domains.Domain.getCertId() == c.Id));
        btnCertViewRequest.setEnabled((c != null) && (!c.TextRequest.isEmpty()));
        btnCertCreate.setEnabled(true);

        btnCertRemove.setEnabled(false);
    }
    public void resetKeywordView(){

        btnKeywordDelete.setEnabled(false);
        btnKeywordAdd.setEnabled(false);
        txtKeywordName.setText("");
        txtKeywordValue.setText("");
        lstKeywords.setModel(new DefaultListModel());
    }
    public void resetCertsView(){
        certsTableModel.Clear();

        btnCertRemove.setEnabled(false);
        btnCertAssign.setEnabled(false);
        btnCertViewRequest.setEnabled(false);
        btnCertCreate.setEnabled(true);
        try {
            btnCertIcon.setDisabledIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/lock_open.png"))));
        } catch (Exception ex){

        }

    }
    public void refreshKeywords(){
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (KeyValue k:Domains.Keywords){
            listModel.addElement(k.getName());
        }

        lstKeywords.setModel(listModel);
        lstKeywords.clearSelection();
        txtKeywordValue.setText("");
        txtKeywordName.setText("");

    }
    public void refreshView(){
        cbDomains.removeAllItems();
        for (Domain dLcv : Domains.List){
            Controller.domainView.cbDomains.addItem(dLcv);
        }
        int idx = Domains.indexOf(Domains.Domain);
        cbDomains.setSelectedIndex(idx);
        refreshKeywords();

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
        btnCertIcon.setDisabledIcon(new ImageIcon(Package.class.getResource("/icons/lock_open.png")));

        txtKeywordValue.setText("");
        txtKeywordName.setText("");
        txtAccountSearch.setText("");
    }
    public void resetUsersView(){
        txtUserFirstName.setText("");
        txtUserLastName.setText("");
        txtUserPassword.setText("");
        txtUserTelephone.setText("");
        txtUserConsumption.setText("");
        spUserQuota.setValue(Long.valueOf(0));
        tpMain.remove(tabAccount);
    }
    public void loadUserView(Account ua){
        usersTableModel.setCurrentUser(ua);
        txtUserFirstName.setText(ua.Me.getFirstName());
        txtUserLastName.setText(ua.Me.getFamilyName());
        txtUserPassword.setText(ua.getPass());
        String Phone;
        String [] Phones;
        Phone=ua.Me.getPhones();

        Phones = ((Phone==null) || (Phone.length()==0)) ?  new String[0] : Phone.split(",") ;
        Phone = (Phones.length>0) ? Phones[0] : "";
        txtUserTelephone.setText(Phone);
        spUserQuota.setValue(Long.valueOf(ua.getQuota()));
        txtUserConsumption.setText(String.valueOf(ua.getConsumption()));


        tpMain.add(tabAccount);
        int idx = tpMain.indexOfComponent(tabAccount);
        tpMain.setTitleAt(idx,Controller.Lang.Domain.getString("label.domain.user"));
        tpMain.setSelectedIndex(idx);

    }
    public void loadView(Domain d){
        Loading=true;
        try {
            int idx = Domains.indexOf(d);
            cbDomains.setSelectedIndex(idx);
            txtDomainNew.setText(d.getName());
            txtFriendlyName.setText(d.getFriendlyName());
            certsTableModel.loadCertificates(d);
            usersTableModel.setDomain(d);
            keywordsViewChanged();
            certsViewChanged();
            usersViewChanged();
            int size = Controller.Configuration.getKeywordSplitterSize();
            if (size <120) size = 120;
            spKeywords.setDividerLocation(size);

        } finally {
            Loading=false;
        }
    }
    public void updateStatusBar(){
        long iValue;
        String sMount;
        iValue =  (Controller.Cloud.Node==null) ? 0 : Controller.Cloud.Node.getId();
        lblNode.setText(String.valueOf(iValue));
        iValue =  (Controller.Cloud.Resource==null) ? 0 : Controller.Cloud.Resource.getId();
        lblResource.setText(String.valueOf(iValue));
        iValue =  (Controller.Cloud.Cluster==null) ? 0 : Controller.Cloud.Cluster.getId();
        lblGroup.setText(String.valueOf(iValue));
        sMount = (Controller.Cloud.Disk==null) ? "" : Controller.Cloud.Disk.getMount();
        sMount = (sMount.length()==0) ? Controller.Lang.Clustering.getString("label.clustering.disk.mount.none") : sMount;
        lblMount.setText(sMount);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
