/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.controller;

import javax.swing.UIManager;
import net.skourtis.sqladmin.view.NewConnectionFrame;
import net.skourtis.sqladmin.view.WorkspaceFrame;

/**
 *
 * @author Stavros
 */
public class NewConnectionController {
    private static NewConnectionFrame frame;
    
    public static void set() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new NewConnectionFrame();
                frame.setVisible(true);
            }
        });
    }

    public static void addConnection(String name, String ip, String port, String username, String password, String schema) {
        
        WorkspaceController.addConnection(name, ip, port, username, password, schema);
        frame.setVisible(false);
    }
}
