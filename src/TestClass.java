
import com.sun.xml.internal.bind.v2.TODO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class TestClass {

    /*
     *  Oracle
     */
    static String orDriverClassName = "oracle.jdbc.OracleDriver";
    static String orUrl = "jdbc:oracle:thin:@195.251.123.227:1521:dblabs";
    static Connection orDbConnection = null;
    static String orUsername = "dblab_45";
    static String orPasswd = "dbuser27580";
    static Statement orStatement = null;

    public static void main(String args[]) throws Exception {
        Class.forName(orDriverClassName);

        
        orDbConnection = DriverManager.getConnection(orUrl, orUsername, orPasswd);

        Statement statement = orDbConnection.createStatement();
        

        //ResultSet set =orDbConnection.getMetaData().getTables("%", orUsername.toUpperCase(), "%", new String[]{"TABLE"});
       // ResultSet set =orDbConnection.getMetaData().getFunctions("%", orUsername.toUpperCase(), null);
        
       // ResultSet set = orDbConnection.getMetaData().getProcedures(null, orUsername.toUpperCase(), "%");
        //ResultSet set =orDbConnection.getMetaData().getTables("%", orUsername.toUpperCase(), "%", new String[]{"TRIGGER"});
        
        ResultSet set = orDbConnection.getMetaData().getImportedKeys(null,orUsername.toUpperCase(), "CLIENT");
        
        int counter = 0;
        while(set.next()){
            
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                System.out.println(set.getMetaData().getColumnName(i)+" :"+set.getString(i));
            }
            System.out.println("\n\n");
            counter++;
        }
        System.out.println(counter);
        
        ResultSet set2 = orDbConnection.getMetaData().getPrimaryKeys(null,orUsername.toUpperCase(), "CLIENT");
        
        
        counter = 0;
        while(set2.next()){
            
            for (int i = 1; i <= set2.getMetaData().getColumnCount(); i++) {
                System.out.println(set2.getMetaData().getColumnName(i)+" :"+set2.getString(i));
            }
            System.out.println("\n\n");
            counter++;
        }
        System.out.println(counter);
        orDbConnection.close();

    }
}
