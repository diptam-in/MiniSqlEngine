
import filepkg.MultipleAggregateFunctionException;
import filepkg.MyFileReader;
import filepkg.TableDoesNotExistExecption;
import java.io.IOException;
import java.util.Vector;
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
    public static void main(String args[]) throws IOException, TableDoesNotExistExecption, MultipleAggregateFunctionException
    {
        
        MyParser prsr = new MyParser();
        MyFileReader fr = new MyFileReader();
        
        String query="";
        for(String s : args)
            query+=s+" ";
        
        System.out.println("query: "+query+"\n\n");
        prsr.setQuery(query);
        Vector <String> header = prsr.getListOfColumns();
        Vector< Vector<String> > Result = fr.getRecordSet(header,prsr.getListOfTables(),prsr.getConditionList(),prsr.getClauseType());
    
        for(String s: header)
            System.out.print(s+"\t");
        System.out.println();
        
        for(int i=0;i<Result.size();i++)
        {
            for(int j=0;j<Result.get(i).size();j++)
                System.out.print(Result.get(i).get(j)+"\t");
            System.out.println();
        }
    
    }
}
