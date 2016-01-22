/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;
import net.skourtis.sqladmin.utils.StringUtils;

/**
 *
 * @author Stavros
 */
public class Table {

    private String name;
    private LinkedList<Column> columns;
    private LinkedList<Constraint> constraints;
    private Database parent;

    public Table(Database parent) {
        this.parent = parent;
        columns = new LinkedList<>();
        constraints = new LinkedList<>();
    }

    public Table(String name, Database parent) {
        this(parent);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Column> getColumns() {
        return columns;
    }

    public LinkedList<Constraint> getConstrains() {
        return constraints;
    }

    public void refresh() throws SQLException {
        columns.clear();
        constraints.clear();

        Connection connection = parent.getConnection();
        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery("select * from " + name);

        ResultSetMetaData meta = set.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            Column col = new Column();
            col.setName(meta.getColumnName(i));
            col.setNullable(meta.isNullable(i) == ResultSetMetaData.columnNullable);
            col.setAutoIncrement(meta.isAutoIncrement(i));
            col.setTypeName(meta.getColumnTypeName(i));
            col.setType(meta.getColumnType(i));

            columns.add(col);
        }

        while (set.next()) {
            for (Column col : columns) {
                if (col.getTypeName().equals("CHAR") || col.getTypeName().equals("VARCHAR2") || col.getTypeName().equals("LONG")) {

                    col.addRow(set.getString(col.getName()));
                } else if (col.getTypeName().equals("NUMBER")) {

                    col.addRow(String.valueOf(set.getDouble(col.getName())));
                } else if (col.getTypeName().equals("FLOAT")) {

                    col.addRow(String.valueOf(set.getFloat(col.getName())));
                } else if (col.getTypeName().equals("RAW") || col.getTypeName().equals("LONGDRAW")) {

                    col.addRow(StringUtils.getHex(set.getBytes(col.getName())));
                } else if (col.getTypeName().equals("DATE")) {

                    String temp = set.getDate(col.getName()).toString();
                    col.addRow(temp);
                } else if (col.getTypeName().equals("TIMESTAMP")) {
                    String temp = set.getTimestamp(col.getName()).toString();
                    col.addRow(temp);
                } else {

                    col.addRow("type not defined");
                }
            }
        }
        set.close();
        statement.close();

        ResultSet set2 = connection.getMetaData().getImportedKeys(null, parent.getUsername().toUpperCase(), name);

        while (set2.next()) {
            Constraint con = new Constraint();
            for (int i = 1; i <= set2.getMetaData().getColumnCount(); i++) {
                con.set(set2.getMetaData().getColumnName(i), set2.getString(i));
            }
            constraints.add(con);
        }

        ResultSet set3 = connection.getMetaData().getPrimaryKeys(null, parent.getUsername().toUpperCase(), name);

        while (set3.next()) {
            Constraint con = new Constraint();
            for (int i = 1; i <= set3.getMetaData().getColumnCount(); i++) {
                con.set(set3.getMetaData().getColumnName(i), set3.getString(i));
            }
            constraints.add(con);
        }

        set2.close();
        set3.close();

//        Statement state = connection.createStatement();
//        
//        ResultSet basicInfo = state.executeQuery("SELECT * FROM user_cons_columns where table_name='"+name+"'");
//        
//        
//        while(basicInfo.next()){
//            Constraint constraint = new Constraint();
//            
//            constraint.set("OWNER",basicInfo.getString("OWNER"));
//            constraint.set("CONSTRAINT_NAME",basicInfo.getString("CONSTRAINT_NAME"));
//            constraint.set("COLUMN_NAME",basicInfo.getString("COLUMN_NAME"));
//            constraint.set("POSITION",basicInfo.getString("POSITION"));
//            
//            constraints.add(constraint);
//        }
//        basicInfo.close();
//        state.close();
//        
//        
//        PreparedStatement query = connection.prepareStatement("SELECT * FROM user_constraints where constraint_name=?");
//        
//        for(Constraint con : constraints){
//            query.setString(1, con.get("CONSTRAINT_NAME"));
//            ResultSet conSet = query.executeQuery();
//            
//            while(conSet.next()){
//                con.set("CONSTRAINT_TYPE",conSet.getString("CONSTRAINT_TYPE"));
//                con.set("SEARCH_CONDITION",conSet.getString("SEARCH_CONDITION"));
//                con.set("R_OWNER",conSet.getString("R_OWNER"));
//                con.set("R_CONSTRAINT_NAME",conSet.getString("R_CONSTRAINT_NAME"));
//                con.set("DELETE_RULE",conSet.getString("DELETE_RULE"));
//                con.set("STATUS",conSet.getString("STATUS"));
//                con.set("DEFERRABLE",conSet.getString("DEFERRABLE"));
//                con.set("DEFERRED",conSet.getString("DEFERRED"));
//                con.set("VALIDATED",conSet.getString("VALIDATED"));
//                con.set("GENERATED",conSet.getString("GENERATED"));
//                con.set("BAD",conSet.getString("BAD"));
//                con.set("RELY",conSet.getString("RELY"));
//                con.set("LAST_CHANGE",conSet.getString("LAST_CHANGE"));
//                con.set("INDEX_OWNER",conSet.getString("INDEX_OWNER"));
//                con.set("INDEX_NAME",conSet.getString("INDEX_NAME"));
//                con.set("INVALID",conSet.getString("INVALID"));
//                con.set("VIEW_RELATED",conSet.getString("VIEW_RELATED"));
//            }
//            conSet.close();
//            
//        }
//        query.close();
    }
}
