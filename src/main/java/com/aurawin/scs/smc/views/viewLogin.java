package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.core.solution.Table;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.controllers.DialogCompletion;
import com.aurawin.scs.smc.controllers.OnDialogCompletion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.*;

import static com.aurawin.core.solution.DBMSMode.*;
import static com.aurawin.scs.smc.views.DialogMode.dmError;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class viewLogin {
    public JPanel mainPanel;
    public JPasswordField txtPassword;
    public JTextField txtUsername;
    private JPanel pnlCredentials;
    private JPanel tabLogin;
    private JPanel tabDBMS;
    public JTextField txtHostname;
    public JTextField txtSchema;
    public JComboBox cbCharset;
    private JPanel pnlDrivers;
    public JRadioButton rbDriverMySQL;
    public JRadioButton rbDriverPostgres;
    public JRadioButton rbDriverMS;
    public JRadioButton rbdriverOracle;
    private JPanel tabMatrix;
    private JTabbedPane tpLogin;
    private JPanel pnlLogin;
    private JButton cancelButton;
    private JButton btnLogin;
    public JTextField txtClusterId;
    private JLabel lblResourceId;
    private JLabel lblNodeId;
    private JLabel lblClusterId;
    public JTextField txtResourceId;
    public JTextField txtNodeId;
    public JTextField txtPort;
    private JLabel lblPort;
    private JLabel lblNotice;

    public viewLogin() {
        lblNotice.setVisible(false);
        cbCharset.removeAllItems();
        String [] itms = Table.DBMS.Encoding.split(",");
        for (String s:itms){
            cbCharset.addItem(s);

        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.frameMain.dispatchEvent(new WindowEvent(Controller.frameMain, WindowEvent.WINDOW_CLOSING));
            }
        });

        txtUsername.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtUsername.getText()!=null) && (!txtUsername.getText().isEmpty()))
                    Controller.Configuration.setUsername(txtUsername.getText());
            }
        });

        txtPassword.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtPassword.getPassword()!=null) && (txtPassword.getPassword().length>0))
                    Controller.Configuration.setPassword(new String(txtPassword.getPassword()));
            }
        });

        txtHostname.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtHostname.getText()!=null) && (!txtHostname.getText().isEmpty()))
                    Controller.Configuration.setHostname(txtHostname.getText());
            }
        });

        txtPort.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtPort.getText()!=null) && (!txtPort.getText().isEmpty()))
                    Controller.Configuration.setPort(Integer.parseInt(txtPort.getText()));
            }
        });

        txtSchema.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtSchema.getText()!=null) && (!txtSchema.getText().isEmpty()))
                    Controller.Configuration.setSchema(txtSchema.getText());
            }
        });


        rbDriverMySQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Controller.Configuration.setMode(dbmsmMySQL);
            }
        });
        rbDriverMS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.Configuration.setMode(dbmsmMicrosoft);
            }
        });
        rbdriverOracle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.Configuration.setMode(dbmsmOracle);
            }
        });
        rbDriverPostgres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.Configuration.setMode(dbmsmPostgreSQL);
            }
        });


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblNotice.setVisible(false);
                btnLogin.setEnabled(false);
                if (Controller.Connect()) {
                    Controller.frameLogin.setDefaultCloseOperation(HIDE_ON_CLOSE);
                    Controller.frameLogin.dispatchEvent(new WindowEvent(Controller.frameLogin, WindowEvent.WINDOW_CLOSING));
                    Controller.loggedIn();
                    Controller.frameMain.setVisible(true);
                    btnLogin.setEnabled(true);

                } else {
                    btnLogin.setEnabled(true);
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){}
                            },
                            dmError,
                            Controller.Lang.Dialog.getString("dmms.login.failure.title"),
                            Controller.Lang.Dialog.getString("dmms.login.failure.message")
                    );
                    Controller.frameLogin.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    lblNotice.setVisible(true);
                }
            }
        });

        txtClusterId.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtClusterId.getText()!=null) && (!txtClusterId.getText().isEmpty()))
                    Controller.Configuration.setClusterId(Long.parseLong(txtClusterId.getText()));
            }
        });
        txtResourceId.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtResourceId.getText()!=null) && (!txtResourceId.getText().isEmpty()))
                    Controller.Configuration.setResourceId(Long.parseLong(txtResourceId.getText()));
            }
        });
        txtNodeId.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if ((txtNodeId.getText()!=null) && (!txtNodeId.getText().isEmpty()))
                    Controller.Configuration.setNodeId(Long.parseLong(txtNodeId.getText()));
            }
        });

        cbCharset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbCharset.getSelectedIndex()!=-1)
                    Controller.Configuration.setEncoding(cbCharset.getSelectedItem().toString());
            }
        });
    }
}
