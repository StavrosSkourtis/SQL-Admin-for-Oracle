package net.skourtis.sqladmin;

import java.sql.SQLException;
import java.util.LinkedList;
import net.skourtis.sqladmin.controller.WorkspaceController;
import net.skourtis.sqladmin.model.Column;
import net.skourtis.sqladmin.model.Database;
import net.skourtis.sqladmin.model.Function;
import net.skourtis.sqladmin.model.Procedure;
import net.skourtis.sqladmin.model.Table;
import net.skourtis.sqladmin.model.Trigger;
import net.skourtis.sqladmin.model.Type;

public class Launcher {

    public static void main(String args[]) throws SQLException {

        WorkspaceController.set();

//        Database db = new Database("test", "195.251.123.227", "dblabs", "1521", "dblab_45", "dbuser27580");
//        db.connect();
//        db.refresh();
//        
//        
//        for( Type function : db.getTypes()){
//            System.out.println(function.getCode());
//        }
//        
//        
//        
//        db.close();
    }

}
