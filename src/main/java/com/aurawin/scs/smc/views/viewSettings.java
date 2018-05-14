package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.models.ContentTypeTableModel;
import com.aurawin.scs.smc.models.DNSTableModel;
import com.aurawin.scs.stored.ContentType;
import com.aurawin.scs.stored.DNS;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Group;
import org.hibernate.annotations.Cascade;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.core.stored.entities.Entities.UseNewTransaction;

public class viewSettings {
    public  JPanel mainPanel;
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
    private JTabbedPane tpSettings;
    private JPanel tabDNS;
    private JPanel pnlKeywordTools;
    private JLabel lblKeywordName;
    private JButton btnDNSDelete;
    private JButton btnDNSAdd;
    private JButton btnDNSSave;
    private JPanel tabContentType;
    private JTable tblSettingsContentType;
    private JTable tblSettingsDNS;
    private JButton btnContentTypeDelete;
    private JButton btnContentTypeAdd;
    private JButton btnContentTypeSave;
    private JTextField txtContentType;
    private JTextField txtContentTypeExt;
    private JTextField txtDNSServer;

    protected boolean navigationExpanded;
    protected ContentTypeTableModel ctTableModel;
    protected DNSTableModel dnsTableModel;

    public viewSettings() {
        navigationExpanded=true;
        ctTableModel = new ContentTypeTableModel(tblSettingsContentType);
        dnsTableModel = new DNSTableModel(tblSettingsDNS);

        tblSettingsContentType.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int idx = tblSettingsContentType.getSelectedRow();
                    if (idx!=-1) {
                        setContentTypeView(ctTableModel.getContentType(idx));
                    }

                }
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

        txtContentType.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String[] ctv = txtContentType.getText().split("/");
                String cte = txtContentTypeExt.getText();
                boolean enName = ( (ctv!=null) && (ctv.length==2));
                boolean selected = tblSettingsContentType.getSelectedRow()!=-1;
                if (enName) {
                    boolean enNotExist = Entities.Settings.ContentType.Lookup(ctv[0],ctv[1],cte)==null;
                    btnContentTypeAdd.setEnabled(enNotExist);
                    btnContentTypeSave.setEnabled(selected);
                    btnContentTypeDelete.setEnabled(selected);
                } else {
                    btnContentTypeAdd.setEnabled(false);
                    btnContentTypeSave.setEnabled(false);
                    btnContentTypeDelete.setEnabled(false);
                }

            }
        });
        txtContentTypeExt.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String[] ctv = txtContentType.getText().split("/");
                String cte = txtContentTypeExt.getText();
                boolean enName = ( (ctv!=null) && (ctv.length==2));
                boolean selected = tblSettingsContentType.getSelectedRow()!=-1;
                if (enName) {
                    boolean enNotExist = Entities.Settings.ContentType.Lookup(ctv[0],ctv[1],cte)==null;
                    btnContentTypeAdd.setEnabled(enNotExist);
                    btnContentTypeSave.setEnabled(selected);
                    btnContentTypeDelete.setEnabled(selected);
                } else {
                    btnContentTypeAdd.setEnabled(false);
                    btnContentTypeSave.setEnabled(false);
                    btnContentTypeDelete.setEnabled(false);
                }

            }
        });

        btnContentTypeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] ctv = txtContentType.getText().split("/");
                boolean enName = ( (ctv!=null) && (ctv.length==2));
                if (enName) {
                    ContentType ct = new ContentType();
                    ct.setMajor(ctv[0]);
                    ct.setMinor(ctv[1]);
                    ct.setExt(txtContentTypeExt.getText());
                    ctTableModel.addContentType(ct);
                }

            }
        });
        btnContentTypeDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblSettingsContentType.getSelectedRow();
                if (idx!=-1) {
                    ContentType ct = ctTableModel.getContentType(idx);
                    Entities.Delete(ct,CascadeOn,UseNewTransaction);
                    ctTableModel.removeContentType(ct);
                }
            }
        });

        txtDNSServer.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sH = txtDNSServer.getText();

                boolean enH = ( (sH!=null) && (sH.length()>=7));
                if (enH) {
                    String[] saH4 = sH.split(".");
                    String[] saH6 = sH.split(".");
                    enH = ((saH4.length == 4) || (saH6.length > 4));
                }
                boolean selected = tblSettingsDNS.getSelectedRow()!=-1;
                if (enH) {
                    boolean enNotExist = Entities.Settings.DNS.Lookup(IpHelper.toLong(sH))==null;
                    btnContentTypeAdd.setEnabled(enNotExist);
                    btnContentTypeSave.setEnabled(selected);
                    btnContentTypeDelete.setEnabled(selected);
                } else {
                    btnContentTypeAdd.setEnabled(false);
                    btnContentTypeSave.setEnabled(false);
                    btnContentTypeDelete.setEnabled(false);
                }

            }
        });
        btnContentTypeSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnDNSSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblSettingsDNS.getSelectedRow();
                if (idx!=-1){
                    DNS d = dnsTableModel.getDNS(idx);
                    if (d!=null){
                        d.setHost(IpHelper.toLong(txtDNSServer.getText()));
                        dnsTableModel.hostUpdated(d);
                    }
                }

            }
        });
        btnDNSAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] saH = txtDNSServer.getText().split("\\.");
                boolean enName = ( (saH!=null) && (saH.length==4));
                if (enName) {
                    DNS d = new DNS();
                    d.setHost(IpHelper.toLong(txtDNSServer.getText()));
                    dnsTableModel.hostCreated(d);
                }
            }
        });
        btnDNSDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tblSettingsDNS.getSelectedRow();
                if (idx!=-1) {
                    DNS d = dnsTableModel.getDNS(idx);
                    dnsTableModel.hostDeleted(d);
                }
            }
        });
    }


    public void setContentTypeView(ContentType ct){
        if (ct!=null) {
            txtContentType.setText(String.join(ct.getMajor(), "/", ct.getMinor()));
            txtContentTypeExt.setText(ct.getExt());
        } else {
            txtContentType.setText("");
            txtContentTypeExt.setText("");
        }
    }
    public void refreshDNSView(){
        dnsTableModel.refreshHosts();
    }
    public void refreshContentTypeView(){
        ctTableModel.refreshView();
    }
    public void refreshViews(){
        refreshContentTypeView();
        refreshDNSView();
    }
}
