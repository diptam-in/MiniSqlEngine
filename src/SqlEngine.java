
import filepkg.MyFileReader;
import java.io.IOException;
import parserpkg.MyParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diptam
 */
public class SqlEngine {
    public static void main(String args[]) throws IOException
    {
        
        MyParser prsr = new MyParser();
        MyFileReader fr = new MyFileReader();
        
        String query="";
        for(String s : args)
            query+=s+" ";
        
        System.out.println("query: "+query);
        prsr.setQuery(query);
        fr.getRecordSet(prsr.getListOfColumns(),prsr.getListOfTables(),prsr.getClause());
    }
}
