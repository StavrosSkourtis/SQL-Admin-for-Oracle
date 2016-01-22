/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import net.skourtis.sqladmin.model.Column;
import net.skourtis.sqladmin.model.Database;
import net.skourtis.sqladmin.view.WorkspaceFrame;
import net.skourtis.xmlparser.XmlElementNode;
import net.skourtis.xmlparser.XmlNode;
import net.skourtis.xmlparser.XmlParser;

/**
 *
 * @author Stavros
 */
public class WorkspaceController {

    public static HashMap<String, Database> connections = new HashMap<>();
    public static WorkspaceFrame frame;
    public static String selectedDb;

    public static void set() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new WorkspaceFrame();
                frame.setVisible(true);

                new Thread() {
                    public void run() {
                        frame.appendOutputln("Loading connections from config.xml..");
                        File file = new File("config.xml");

                        if (file.exists()) {
                            try {
                                XmlParser parser = new XmlParser(file);

                                XmlElementNode root = parser.parse();
                                

                                LinkedList<XmlNode> nodes = root.getSubElements("connection");

                                for (XmlNode node : nodes) {
                                    XmlElementNode conNode = (XmlElementNode) node;
                                    addConnection(conNode.getAttribute("name"),
                                            conNode.getAttribute("ip"),
                                            conNode.getAttribute("port"),
                                            conNode.getAttribute("user"),
                                            conNode.getAttribute("password"),
                                            conNode.getAttribute("schema"));
                                }

                            } catch (IOException ex) {
                                frame.appendOutputln("Error at parsing config :" + ex.getMessage());
                            }

                        } else {
                            frame.appendOutputln("config.xml does not exist.");
                        }
                    }

                }.start();
            }
        });

    }

    public static void newConnection() {
        /*
         Start New Connection Controller
         */
        NewConnectionController.set();
    }

    public static void addConnection(String name, String ip, String port, String username, String password, String schema) {

        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                /*
                 Create the db
                 */
                Database db = new Database(name, ip, schema, port, username, password);
                try {
                    frame.appendOutputln("Connecting to " + name + "...");
                    db.connect();
                    frame.appendOutputln("Fetching data ...");
                    db.refresh();

                    /*
                     Insert it to the hash map
                     */
                    selectedDb = name;
                    frame.setSelected(selectedDb);
                    connections.put(name, db);
                    /*
        
                     */
                    frame.appendOutputln("Connection successful!");
                    frame.refresh();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    frame.appendOutputln(ex.getMessage());
                } catch (ClassNotFoundException e) {
                }
                frame.stopLoading();
            }

        }.start();

    }

    public static void exexuteQuery(String sql) {
        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                if (selectedDb == null) {
                    frame.appendOutputln("No Connection Selected..");
                    return;
                }

                try {
                    long before = System.currentTimeMillis();
                    LinkedList<Column> cols = connections.get(selectedDb).executeQuery(sql);
                    long after = System.currentTimeMillis();

                    frame.setQueryResult(cols);
                    frame.setOutputPane(1);

                } catch (SQLException e) {
                    frame.setOutputPane(0);
                    frame.appendOutputln(e.getMessage());
                }

                frame.stopLoading();
            }
        }.start();

    }
    
    public static void executeUpdate(String query){
        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                if (selectedDb == null) {
                    frame.appendOutput("No Connection Selected..");
                    return;
                }

                try {
                    long before = System.currentTimeMillis();
                    connections.get(selectedDb).executeUpdate(query);
                    long after = System.currentTimeMillis();

                    frame.setOutputPane(0);
                    frame.appendOutputln("Query Executed Successfuly in "+(after-before)+"ms");

                } catch (SQLException e) {
                    frame.setOutputPane(0);
                    frame.appendOutputln(e.getMessage());
                }

                frame.stopLoading();
            }
        }.start();
    }

    public static void refreshCurrentConnection() {

        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                frame.setOutputPane(0);
                if (selectedDb == null) {
                    frame.appendOutputln("No connection selected");
                    return;
                }

                try {
                    frame.appendOutputln("Refreshing " + selectedDb + "...");
                    connections.get(selectedDb).refresh();
                    frame.appendOutputln("Refresh completed successfuly!");
                } catch (SQLException ex) {
                    frame.appendOutput(selectedDb);
                }
                frame.stopLoading();
            }
        }.start();

    }

    public static void close() {
        for (Database db : connections.values()) {
            db.close();
        }
        System.exit(0);
    }

    public static void closeSelected() {

        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                frame.setOutputPane(0);
                if (selectedDb == null) {
                    frame.appendOutputln("No connection selected");
                    return;
                }

                frame.appendOutputln("Closing " + selectedDb + "...");
                connections.get(selectedDb).close();
                connections.remove(selectedDb);
                selectedDb = null;
                frame.setSelected("Selected connection : ");
                frame.refresh();
                frame.appendOutputln("Connection closed successfuly!");
                frame.stopLoading();
            }
        }.start();

    }

    public static void saveConnections() {
        new Thread() {
            @Override
            public void run() {
                frame.appendOutputln("Saving connections");

                XmlElementNode root = new XmlElementNode();
                root.setName("connections");

                for (Database db : connections.values()) {
                    XmlElementNode connection = new XmlElementNode();
                    connection.setName("connection");

                    connection.addAttribute("name", db.getName());
                    connection.addAttribute("ip", db.getIp());
                    connection.addAttribute("port", db.getPort());
                    connection.addAttribute("user", db.getUsername());
                    connection.addAttribute("password", db.getPassword());
                    connection.addAttribute("schema", db.getSchema());

                    root.add(connection);
                }

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("config.xml"));
                    writer.write(root.toString());
                    writer.close();

                    frame.appendOutputln("Save complete.");
                } catch (IOException e) {
                    frame.appendOutputln("An error occurred while saving : "+e.getMessage());
                }
            }
        }.start();
    }

    public static void showSelectedDetails() {

        new Thread() {
            @Override
            public void run() {
                frame.startLoadind();
                frame.setOutputPane(0);
                if (selectedDb == null) {
                    frame.appendOutputln("No connection selected");
                    return;
                }
                frame.appendOutputln("Connection " + connections.get(selectedDb).getName());
                frame.appendOutputln("At " + connections.get(selectedDb).getIp() + ":" + connections.get(selectedDb).getPort());
                frame.appendOutputln("User " + connections.get(selectedDb).getUsername());
                frame.appendOutputln("Schema " + connections.get(selectedDb).getSchema());
                frame.stopLoading();
            }
        }.start();

    }
    
    
    public static void saveToFile(String filePath ,String text){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(text);
            writer.close();
            frame.appendOutputln("Saving to "+filePath+" completed.");
        }catch (IOException e){
            
        }
    }

}
