package com.aurawin.scs.smc;



import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Controller {
    public static JFrame frameMain;

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

        frameMain = new JFrame("mainPanel");

        frameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameMain.setPreferredSize(new Dimension(800, 600));
        frameMain.setTitle(System.getProperty("program.title"));
        frameMain.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frameMain.setLocation(dim.width/2-frameMain.getSize().width/2, dim.height/2-frameMain.getSize().height/2);

        frameMain.setVisible(true);
    }
}