package com.aurawin.scs.smc.views;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.Package;
import com.aurawin.scs.smc.controllers.DialogCompletion;
import com.aurawin.scs.smc.controllers.OnDialogCompletion;

import javax.swing.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.aurawin.scs.smc.Controller.dialogView;
import static com.aurawin.scs.smc.Controller.frameDialog;

public class viewDialog {
    private JButton btnCancel;
    private JButton btnConfirm;
    private JButton btnIcon;
    private JLabel lblTitle;
    public JPanel mainPanel;
    private JEditorPane epMessage;
    private DialogMode Mode;
    private DialogCompletion Completion;

    public viewDialog() {
        Completion = new DialogCompletion();
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Completion.Canceled = true;
                Completion.Confirmed = false;
                frameDialog.setVisible(false);
                Controller.dialogEnabledFrames();
                Completion.Method.Complete(Completion);

            }
        });
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Completion.Canceled = false;
                Completion.Confirmed = true;
                frameDialog.setVisible(false);
                Controller.dialogEnabledFrames();
                Completion.Method.Complete(Completion);
            }
        });

    }

    public void showDialog(OnDialogCompletion completion, DialogMode dm, String Title, String Message) {


        Completion.Method = completion;

        frameDialog.getContentPane().invalidate();
        frameDialog.validate();


        setMode(dm);
        lblTitle.setText(Title);
        epMessage.setText(Message);

        frameDialog.setVisible(true);


    }

    private void setMode(DialogMode dm) {
        Completion.Canceled = false;
        Completion.Confirmed = false;
        Mode = dm;
        switch (dm) {
            case dmConfirmation:
                frameDialog.setTitle(Controller.Lang.Dialog.getString("title_question"));
                if ((lblTitle.getText() == null) || (lblTitle.getText().length() == 0))
                    lblTitle.setText(Controller.Lang.Dialog.getString("label_question"));
                btnIcon.setDisabledIcon(new ImageIcon(Package.class.getResource("/icons/question.png")));

                break;
            case dmError:
                frameDialog.setTitle(Controller.Lang.Dialog.getString("title_error"));
                if ((lblTitle.getText() == null) || (lblTitle.getText().length() == 0))
                    lblTitle.setText(Controller.Lang.Dialog.getString("label_error"));
                btnIcon.setDisabledIcon(new ImageIcon(Package.class.getResource("/icons/exclamation.png")));
                break;
        }

    }


}
