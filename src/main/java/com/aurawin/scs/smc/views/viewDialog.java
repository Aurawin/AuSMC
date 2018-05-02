package com.aurawin.scs.smc.views;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.Package;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class viewDialog {
    private JButton btnCancel;
    private JButton btnConfirm;
    private JButton btnIcon;
    private JLabel lblTitle;
    public  JPanel mainPanel;
    private JEditorPane epMessage;
    private DialogMode Mode;
    public boolean Canceled;
    public boolean Confirmed;

    public viewDialog() {
        Canceled=false;
        Confirmed=false;
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Canceled=true;
                Confirmed=false;
                Controller.frameDialog.setVisible(false);
            }
        });
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Canceled=false;
                Confirmed=true;
                Controller.frameDialog.setVisible(false);
            }
        });
    }
    public boolean showDialog(DialogMode dm, String Title, String Message){
        setMode(dm);
        lblTitle.setText(Title);
        epMessage.setText(Message);
        Controller.frameDialog.setVisible(true);
        Controller.dialogDisabledFrames();
        Controller.frameLogin.setEnabled(false);
        while (Controller.frameDialog.isShowing()){
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex){
                break;
            }
        }
        Controller.dialogEnabledFrames();
        return Canceled;
    }
    private void setMode(DialogMode dm){
        Canceled=false;
        Mode = dm;
        switch (dm){
            case dmConfirmation:
                Controller.frameDialog.setTitle(Controller.Lang.Dialog.getString("title_question"));
                if ((lblTitle.getText()==null) || (lblTitle.getText().length()==0))
                    lblTitle.setText(Controller.Lang.Dialog.getString("label_question"));
                btnIcon.setDisabledIcon(new ImageIcon(Package.class.getResource("/icons/question.png")));

                break;
            case dmError:
                Controller.frameDialog.setTitle(Controller.Lang.Dialog.getString("title_error"));
                if ((lblTitle.getText()==null) || (lblTitle.getText().length()==0))
                    lblTitle.setText(Controller.Lang.Dialog.getString("label_error"));
                btnIcon.setDisabledIcon(new ImageIcon(Package.class.getResource("/icons/exclamation.png")));
                break;
        }

    }
}
