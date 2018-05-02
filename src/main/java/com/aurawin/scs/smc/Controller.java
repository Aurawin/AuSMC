package com.aurawin.scs.smc;



import com.aurawin.core.Environment;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.smc.controllers.Domains;
import com.aurawin.scs.smc.views.*;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.smc.models.Settings;
import com.aurawin.scs.stored.bootstrap.Bootstrap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.aurawin.core.solution.DBMSMode.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class Controller {
    public static class Lang{
        public static ResourceBundle Dialog;
        public static ResourceBundle DBMS;
        public static ResourceBundle Domain;
    }

    public static final ArrayList<JFrame> Frames = new ArrayList<>();
    public static final String basePackage = "com.aurawin";

    public static Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
    public static Settings Configuration = new Settings();

    public static JFrame frameMain;

    public static JFrame frameLogin;
    public static JFrame frameDialog;
    public static viewClustering clusteringView;
    public static viewDomain domainView;
    public static viewLogin loginView;
    public static viewMain mainView;
    public static viewDialog dialogView;
    private static void setDialogView(){

        JPanel contentPane = (JPanel) frameDialog.getContentPane();
        contentPane.add(dialogView.mainPanel);
        frameDialog.pack();

        frameDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
        frameDialog.setSize(new Dimension(440, 480));
        frameDialog.setLocation(dimScreen.width/2-frameDialog.getSize().width/2, dimScreen.height/2-frameDialog.getSize().height/2);
        frameDialog.setResizable(false);

    }
    private static void setDomainView(){
        frameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPane = (JPanel) frameMain.getContentPane();
        contentPane.remove(mainView.mainPanel);
        contentPane.add(domainView.mainPanel);
        //frameMain.setContentPane(domainView);
        frameMain.pack();

        frameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameMain.setSize(new Dimension(800, 600));
        frameMain.setTitle(System.getProperty("program.title"));
        frameMain.setLocation(dimScreen.width/2-frameMain.getSize().width/2, dimScreen.height/2-frameMain.getSize().height/2);

        //domainView.resetView();
    }
    public static void swapDomainView(){
        frameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel contentPane = (JPanel) frameMain.getContentPane();
        Dimension d = frameMain.getSize();
        contentPane.remove(clusteringView.mainPanel);
        contentPane.add(domainView.mainPanel);
        frameMain.pack();
        frameMain.setSize(d);
        //domainView.mainPanel.repaint();

    }
    public static void swapClusteringView(){
        frameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel contentPane = (JPanel) frameMain.getContentPane();
        Dimension d = frameMain.getSize();
        contentPane.remove(domainView.mainPanel);
        contentPane.add(clusteringView.mainPanel);
        frameMain.pack();
        frameMain.setSize(d);

        clusteringView.refreshView();

        //clusteringView.mainPanel.repaint();
    }
    private static void setupLoginForm(){
        frameLogin.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //frameLogin.setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel contentPane = (JPanel) frameLogin.getContentPane();
        contentPane.add(loginView.mainPanel);

        frameLogin.pack();

        frameLogin.setSize(new Dimension(440, 480));
        frameLogin.setLocation(dimScreen.width/2-frameLogin.getSize().width/2, dimScreen.height/2-frameLogin.getSize().height/2);
        frameLogin.setTitle(System.getProperty("program.title")+" Login");
        frameLogin.setResizable(false);

        loginView.cbCharset.setSelectedItem(Configuration.getEncoding());
        loginView.txtUsername.setText(Configuration.getUsername());
        loginView.txtPassword.setText(Configuration.getPassword());
        loginView.txtHostname.setText(Configuration.getHostname());
        loginView.txtPort.setText(String.valueOf(Configuration.getPort()));
        loginView.txtSchema.setText(Configuration.getSchema());
        switch (Configuration.getMode()){
            case dbmsmMySQL:
                loginView.rbDriverMySQL.setSelected(true);
                break;
            case dbmsmMicrosoft:
                loginView.rbDriverMS.setSelected(true);
                break;
            case dbmsmOracle:
                loginView.rbdriverOracle.setSelected(true);
                break;
            case dbmsmPostgreSQL:
                loginView.rbDriverPostgres.setSelected(true);
                break;
            default:
                break;
        }
        loginView.txtClusterId.setText(String.valueOf(Configuration.getClusterId()));
        loginView.txtResourceId.setText(String.valueOf(Configuration.getResourceId()));
        loginView.txtNodeId.setText(String.valueOf(Configuration.getNodeId()));

        frameLogin.setVisible(true);
    }
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }

        try {
            com.aurawin.core.solution.Settings.Initialize("AuSMC", "Aurawin Managment Console", "Universal");
        } catch(IOException ioe){

        }

        Configuration.Load();
        Controller.Lang.Dialog = ResourceBundle.getBundle("dialog",Locale.getDefault());
        Controller.Lang.DBMS = ResourceBundle.getBundle("dbms",Locale.getDefault());
        Controller.Lang.Domain = ResourceBundle.getBundle("domain",Locale.getDefault());

        frameMain = new JFrame("mainPanel");
        Frames.add(frameMain);

        frameLogin = new JFrame("mainPanel");
        Frames.add(frameLogin);

        frameDialog= new JFrame("mainPanel");
        Frames.add(frameDialog);

        mainView = new viewMain();
        dialogView=new viewDialog();
        loginView = new viewLogin();
        domainView = new viewDomain();
        clusteringView = new viewClustering();

        setupLoginForm();
        setDomainView();
        setDialogView();

        swapDomainView();
    }
    public static boolean Connect(){
        Manifest mf = new Manifest(
                Configuration.getUsername(),                     // username
                Configuration.getPassword(),                     // password
                Configuration.getHostname(),                     // host
                Configuration.getPort(),                         // port
                com.aurawin.core.lang.Database.Config.Automatic.Commit.On,      // autocommit
                2,                                                   // Min Poolsize
                20,                                                 // Max Poolsize
                1,                                                 // Pool Acquire Increment
                50,                                               // Max statements
                10,                                                     // timeout
                Database.Config.Automatic.Update,                               //
                Configuration.getSchema(),                                      // database
                Configuration.getDialect(),                                  // Dialect
                Configuration.getDriver(),                                   // Driver
                Bootstrap.buildAnnotations(com.aurawin.core.Package.class,com.aurawin.scs.Package.class, com.aurawin.scs.smc.Package.class)
        );
        return Entities.Initialize(mf);
    }
    public static void dialogDisabledFrames(){
        for (JFrame f : Frames){
            if (f!=frameDialog){
                f.setEnabled(false);
            }
        }
    }
    public static void dialogEnabledFrames(){
        for (JFrame f : Frames){
            if (f!=frameDialog){
                f.setEnabled(true);
            }
        }
    }
    public static void loggedIn(){
        Domains.loggedIn();
    }
}