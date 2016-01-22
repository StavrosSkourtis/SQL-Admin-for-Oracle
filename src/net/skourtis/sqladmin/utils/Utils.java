/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.utils;

import java.util.LinkedList;
import net.skourtis.sqladmin.model.Column;
import net.skourtis.sqladmin.view.TableModel;

/**
 *
 * @author Stavros
 */
public class Utils {

    public static TableModel dataToTableModel(LinkedList<Column> columns) {
        String[] names = new String[columns.size()];
        String[][] data = new String[columns.get(0).getData().length][columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            names[i] = columns.get(i).getName();
        }

        if (columns.get(0).getData().length > 0) {

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < columns.size(); j++) {
                    try {
                        String temp = columns.get(j).getData()[i];
                        data[i][j] = temp;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        data[i][j] = "undefined";
                    }

                }
            }
        }

        TableModel model = new TableModel(data, names);
        return model;
    }
}
