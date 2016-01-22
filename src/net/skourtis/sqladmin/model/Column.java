/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.model;

import java.util.LinkedList;

/**
 *
 * @author Stavros
 */
public class Column {
    
    public static final int BYTE = 0;
    public static final int SHORT = 1;
    public static final int INT = 2;
    public static final int LONG = 3;
    public static final int FLOAT = 4;
    public static final int DOUBLE = 5;
    public static final int BIGDEC = 6;
    public static final int CHAR = 7;
    public static final int STRING =8;
    public static final int DATE = 9;
    public static final int BOOLEAN = 10;
    public static final int RAW = 11;
    public static final int TIME = 12;
    public static final int TIMESTAMP = 13;
    public static final int BLOB = 0;
    public static final int CLOB = 0;
    public static final int STRUCT = 0;
    
    private String typeName;
    private String name;
    private int type = -1;
    private boolean nullable;
    private boolean autoIncrement;
    private LinkedList<String> data;
    
    public Column(){
        data = new LinkedList<>();
    }
    
    public Column(String name, int type, boolean nullable, boolean autoIncrement) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.autoIncrement = autoIncrement;
        data = new LinkedList<>();
    }
    
    public String[] getData(){
        String [] d = new String[data.size()];
        for(int i=0;i<d.length ;i++)
            d[i] = data.get(i);
        return d;
    }
    
    public void addRow(String row){
        data.add(row);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
    
    
    
}
