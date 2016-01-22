/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.skourtis.sqladmin.utils.StringUtils;

/**
 *
 * @author Stavros
 */
public class Database {

    public static final String driverName = "oracle.jdbc.OracleDriver";

    private String name;
    private String ip;
    private String schema;
    private String port;
    private String username;
    private String password;

    private LinkedList<Table> tables;
    private LinkedList<Trigger> triggers;
    private LinkedList<Function> functions;
    private LinkedList<Procedure> procedures;
    private LinkedList<Type> types;

    private Connection connection;

    Connection getConnection() {
        return connection;
    }

    public Database(String name, String ip, String schema, String port, String username, String password) {
        this.name = name;
        this.ip = ip;
        this.schema = schema;
        this.port = port;
        this.username = username;
        this.password = password;

        tables = new LinkedList<>();
        triggers = new LinkedList<>();
        functions = new LinkedList<>();
        procedures = new LinkedList<>();
        types = new LinkedList<>();
    }

    /**
     * Connects to the database
     */
    public void connect() throws ClassNotFoundException, SQLException {

        Class.forName(driverName);
        String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + schema;
        connection = DriverManager.getConnection(url, username, password);

    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }

    /**
     * Executes a query and returns the result
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public LinkedList<Column> executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(query);

        LinkedList<Column> cols = new LinkedList<>();

        ResultSetMetaData meta = set.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            Column col = new Column();
            col.setName(meta.getColumnName(i));
            col.setNullable(meta.isNullable(i) == ResultSetMetaData.columnNullable);
            col.setAutoIncrement(meta.isAutoIncrement(i));
            col.setTypeName(meta.getColumnTypeName(i));
            col.setType(meta.getColumnType(i));

            cols.add(col);

        }

        while (set.next()) {
            for (Column col : cols) {
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

        statement.close();
        return cols;
    }

    /**
     * Executes the query
     *
     * @param query
     * @throws SQLException
     */
    public void executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate(query);

        statement.close();
    }

    public void refresh() throws SQLException {
        refreshTables();
        refreshFuctions();
        refreshProcedures();
        refreshTypes();
        refreshTriggers();

    }

    /**
     * Recreates the tables list
     */
    public void refreshTables() throws SQLException {
        tables.clear();

        ResultSet tabs = connection.getMetaData().getTables("%", username.toUpperCase(), "%", new String[]{"TABLE"});;

        while (tabs.next()) {
            String name = tabs.getString("TABLE_NAME");
            Table table = new Table(name, this);

            table.refresh();

            tables.add(table);
        }

    }

    public void refreshTypes() throws SQLException {
        types.clear();

        Statement statement = connection.createStatement();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM USER_SOURCE WHERE NAME=?");

        ResultSet funcs = connection.getMetaData().getTables("%", username.toUpperCase(), "%", new String[]{"TYPE"});

        while (funcs.next()) {
            Type type = new Type();
            type.setName(funcs.getString("TABLE_NAME"));

            query.setString(1, type.getName());
            ResultSet code = query.executeQuery();
            StringBuilder builder = new StringBuilder();

            while (code.next()) {
                builder.append(code.getString("TEXT"));
            }

            type.setCode("create or replace " + builder.toString());

            types.add(type);
        }

        query.close();
        statement.close();
    }

    public void refreshTriggers() throws SQLException {
        triggers.clear();

        Statement statement = connection.createStatement();

        PreparedStatement query = connection.prepareStatement("SELECT * FROM USER_SOURCE WHERE NAME=?");

        ResultSet trigs = connection.getMetaData().getTables("%", username.toUpperCase(), "%", new String[]{"TRIGGER"});

        while (trigs.next()) {
            Trigger trigger = new Trigger();

            trigger.setName(trigs.getString("TABLE_NAME"));

            query.setString(1, trigger.getName());
            ResultSet code = query.executeQuery();
            StringBuilder builder = new StringBuilder();

            while (code.next()) {
                builder.append(code.getString("TEXT"));
            }

            trigger.setCode("create or replace " + builder.toString());

            triggers.add(trigger);
        }

        statement.close();

    }

    public void refreshFuctions() throws SQLException {
        functions.clear();

        Statement statement = connection.createStatement();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM USER_SOURCE WHERE NAME=?");

        ResultSet funcs = connection.getMetaData().getProcedures(null, username.toUpperCase(), "%");

        while (funcs.next()) {
            if (funcs.getShort("PROCEDURE_TYPE") != 2) {
                continue;
            }
            Function function = new Function();
            function.setName(funcs.getString("PROCEDURE_NAME"));

            query.setString(1, function.getName());
            ResultSet code = query.executeQuery();
            StringBuilder builder = new StringBuilder();

            while (code.next()) {
                builder.append(code.getString("TEXT"));
            }

            function.setCode("create or replace " + builder.toString());

            functions.add(function);
        }

        query.close();
        statement.close();
    }

    public void refreshProcedures() throws SQLException {
        procedures.clear();

        Statement statement = connection.createStatement();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM USER_SOURCE WHERE NAME=?");

        ResultSet funcs = connection.getMetaData().getProcedures(null, username.toUpperCase(), "%");

        while (funcs.next()) {
            if (funcs.getShort("PROCEDURE_TYPE") != 1) {
                continue;
            }
            Procedure procedure = new Procedure();
            procedure.setName(funcs.getString("PROCEDURE_NAME"));

            query.setString(1, procedure.getName());
            ResultSet code = query.executeQuery();
            StringBuilder builder = new StringBuilder();

            while (code.next()) {
                builder.append(code.getString("TEXT"));
            }

            procedure.setCode("create or replace " + builder.toString());

            procedures.add(procedure);
        }

        query.close();
        statement.close();
    }

    /*
     Get and Set methods
        
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LinkedList<Table> getTables() {
        return tables;
    }

    public LinkedList<Trigger> getTriggers() {
        return triggers;
    }

    public LinkedList<Function> getFuctions() {
        return functions;
    }

    public LinkedList<Procedure> getProcedures() {
        return procedures;
    }

    public LinkedList<Type> getTypes() {
        return types;
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("Table with name " + name + " does not exist");
    }

    public Function getFunction(String name) {
        for (Function table : functions) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("Function with name " + name + " does not exist");
    }

    public Procedure getProcedure(String name) {
        for (Procedure table : procedures) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("Procedure with name " + name + " does not exist");
    }

    public Trigger getTrigger(String name) {
        for (Trigger table : triggers) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("Trigger with name " + name + " does not exist");
    }

    public Type getType(String name) {
        for (Type table : types) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("Type with name " + name + " does not exist");
    }
}
