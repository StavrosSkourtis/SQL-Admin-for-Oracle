/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.view;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Stavros
 */
public class TableModel extends DefaultTableModel{
    
    public TableModel(String[][]data , String[] names){
        super(data, names);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
}
