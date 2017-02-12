/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filepkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import javafx.util.Pair;

/**
 *
 * @author diptam
 */
public class MyFileReader {
    
    private String path = "/home/diptam/DBS/Assignment-1/";
    private Map< String , Vector<String> > metadata = new HashMap();
    public MyFileReader() throws IOException
    {
        this.prepareMetadata();
    }
    
    public Vector< Vector<String> > getRecordSet(Vector<String> Columns, Vector<String> Tables, Vector< Pair<String,Pair<String,String> > > Clause, int Comparison_Type) throws IOException
    {
       Vector< Vector<String> > RecordSet = new Vector();
       Map <String, Vector< Vector<String> > > temp = new HashMap();
       Vector<Integer> colnum;
       int col;
       for(int i = 0; i < Tables.size();i++)
       {
           colnum = new Vector();
           for(int j=0;j<Columns.size();j++)
           { 
               if((col=isInTable(Columns.get(j),Tables.get(i)))>=0)
                  colnum.add(col);
           }
           temp.put(Tables.get(i), getDataFromTable(colnum,Tables.get(i)));
       }
       System.out.println(temp);
       System.out.println(Clause);
       System.out.println(Comparison_Type);
       return RecordSet;
    }
    
    Vector< Vector<String> > getDataFromTable(Vector<Integer> colnum, String tab) throws FileNotFoundException, IOException
    {
        Vector< Vector<String> > res = new Vector(); 
        Vector<String> temp;
        String data;
        int count=0;
        String[] field;
        File tabname = new File(path,tab+".csv");
        FileReader f = new FileReader(tabname);
        BufferedReader in = new BufferedReader(f);
        
        if(colnum.indexOf(999)>=0)
        {
            while((data=in.readLine())!=null)
            {
                temp = new Vector();
                field=data.split(",");
                for(String s : field)
                    temp.add(s);
                res.add(temp);
            }
            return res;
        }
        
        while((data=in.readLine())!=null)
        {
            field=data.split(",");
            temp = new Vector();
            for(int i=0;i<colnum.size();i++)
               temp.add(field[colnum.get(i)]);
            res.add(temp);
        }
        return res;
    }
    
    int isInTable(String col, String tab)
    {
        if(col.equals("*"))
            return 999;
        int loc = metadata.get(tab).indexOf(col);
        return loc;
    }
    
    void prepareMetadata() throws FileNotFoundException, IOException
    {
        String read;
        Vector<String> cols;
        String tab;
        File mtdt = new File(path,"metadata.txt");
        FileReader f = new FileReader(mtdt);
        BufferedReader in = new BufferedReader(f);
        while((read=in.readLine())!=null)
        {
            if(read.equals("<begin_table>"))
            {
                tab=in.readLine();
                read = in.readLine();
                cols = new Vector();
                while(!read.equals("<end_table>"))
                {
                    cols.add(read);
                    read = in.readLine();
                }
                metadata.put(tab, cols);
            }
        }
    }
}
