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
       if(Columns.get(0).equals("*"))
       {
           Columns.clear();
           for(String t: Tables)
           Columns.addAll(metadata.get(t));
       }
       Map <String, Vector< Vector<String> > > table_data = new HashMap();
       Map <String, Vector<String> > table_header = new HashMap();
       Vector <String> header;
       Vector<Integer> colnum;
       
       int col;
       for(int i = 0; i < Tables.size();i++)
       {
           colnum = new Vector();
           header = new Vector();
           
           for(int j=0;j<Columns.size();j++)
           { 
               if((col=isInTable(Columns.get(j),Tables.get(i)))>=0)
               {
                  colnum.add(col);
                  header.add(Columns.get(j));
                  
               }
           }
           
           table_header.put(Tables.get(i), header);
           table_data.put(Tables.get(i), getDataFromTable(colnum,Tables.get(i)));
       }
       System.out.println(table_header);
       System.out.println(table_data);
       System.out.println(Clause);
       System.out.println(Comparison_Type);
       RecordSet = this.applyClauses(table_data, table_header, Tables, Clause, Comparison_Type);
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
    
    Vector< Vector<String> > applyClauses(Map <String, Vector< Vector<String> > > table_data,
                                        Map <String, Vector<String> > table_header,
                                        Vector<String> Tables,
                                        Vector< Pair<String,Pair<String,String> > > Clause,
                                        int Comparison_Type)
    {
        Vector< Vector <String> > res_set = new Vector();
    
        String data1,data2,tab1,tab2;
        int index1,index2;
        Vector<Vector<String> > all_data_clause;
        Vector<String> data_clause;
        Vector<Pair<Integer,Integer> > all_index = new Vector();
        Vector<Pair<String,String> > all_table_of_index = new Vector();
        Pair<Integer,Integer> temp_index;
        Pair<String,String> temp_table_of_index;
        
        for(int i=0;i<Clause.size();i++)
        {
          index1=-1;index2=-1;
          
          data1=Clause.get(i).getValue().getKey();data2=Clause.get(i).getValue().getValue();
          tab1 = this.inTable(data1,Tables);tab2 = this.inTable(data2,Tables);
          
          if(tab1!=null)
            index1 = table_header.get(tab1).indexOf(data1);
          if(tab2!=null)
            index2 = table_header.get(tab2).indexOf(data2);
          
          temp_index = new Pair(index1,index2);
          temp_table_of_index = new Pair(tab1,tab2);
          
          all_index.add(temp_index);
          all_table_of_index.add(temp_table_of_index);
        }
        
        System.out.println("All Indexes: "+ all_index);
        System.out.println("All Table_of_Indexes: "+ all_table_of_index);
        /* When Number of table is one*/
        if(Tables.size()==1) 
        {
            for(int j=0; j < table_data.get(Tables.get(0)).size();j++)
            {
                data1=null;data2=null;
                all_data_clause = new Vector();
                for(int c=0;c<Clause.size();c++)
                {
                    data1=Clause.get(c).getValue().getKey();data2=Clause.get(c).getValue().getValue();
                    if(all_index.get(c).getKey()>=0)
                        data1= table_data.get(all_table_of_index.get(c).getKey()).get(j).get(all_index.get(c).getKey());
                    if(all_index.get(c).getValue()>=0)
                        data2= table_data.get(all_table_of_index.get(c).getValue()).get(j).get(all_index.get(c).getValue());
                    data_clause = new Vector();
                    data_clause.add(Clause.get(0).getKey());data_clause.add(data1);data_clause.add(data2);
                    all_data_clause.add(data_clause);
                }
                
                if(evaluateComparison(all_data_clause,Comparison_Type))
                    res_set.add(table_data.get(Tables.get(0)).get(j)); 
            }
        }
        /*When Number of tables is two*/
        if(Tables.size()==2)
        {   int m=0;
            for(int j=0; j < table_data.get(Tables.get(0)).size();j++)
            {
                for(int k =0; k < table_data.get(Tables.get(1)).size();k++)
                {
                data1=null;data2=null;
                all_data_clause = new Vector();
                    for(int c=0;c<Clause.size();c++)
                    {
                        m=k;
                        data1=Clause.get(c).getValue().getKey();data2=Clause.get(c).getValue().getValue();
                        if(all_index.get(c).getKey()>=0)
                        {
                            if(Tables.indexOf(all_table_of_index.get(c).getKey())==0) m=j;
                            data1= table_data.get(all_table_of_index.get(c).getKey()).get(m).get(all_index.get(c).getKey());
                        }
                        if(all_index.get(c).getValue()>=0)
                        {
                            if(Tables.indexOf(all_table_of_index.get(c).getValue())==0) m=j;
                            data2= table_data.get(all_table_of_index.get(c).getValue()).get(m).get(all_index.get(c).getValue());
                        }
                        data_clause = new Vector();
                        data_clause.add(Clause.get(0).getKey());data_clause.add(data1);data_clause.add(data2);
                        all_data_clause.add(data_clause);
                    }
                    if(evaluateComparison(all_data_clause,Comparison_Type))
                    {                           
                        Vector<String> temp_tab2 = new Vector(table_data.get(Tables.get(1)).get(k));
                        Vector<String> temp_tab1 = new Vector(table_data.get(Tables.get(0)).get(j));
                        temp_tab1.addAll(temp_tab2);
                        res_set.add(temp_tab1); 
                    }
                }
            }
        }
      return res_set;
    }
    
    boolean evaluateComparison(String operator, String op1, String op2)
    {
        Integer a = new Integer(op1);
        Integer b = new Integer(op2);
        switch(operator)
        {
            case "=" : return a.equals(b);
            case "<" : return (a<b);
            case ">" : return (a>b);
            case "<=" : return (a<=b);
            case ">=" : return (a>=b);
            case "!=" : return !a.equals(b);
            default : return true;
        }
    }
    
    boolean evaluateComparison(Vector< Vector <String> > Clause, int comparison_type)
    {
        boolean temp,res;
 
        Integer a = new Integer(Clause.get(0).get(1));
        Integer b = new Integer(Clause.get(0).get(2));
        switch(Clause.get(0).get(0))
        {
            case "=" : res= a.equals(b); break;
            case "<" : res=(a<b); break;
            case ">" : res=(a>b); break;
            case "<=" : res=(a<=b); break;
            case ">=" : res=(a>=b); break;
            case "!=" : res=!a.equals(b); break;
            default : res=true;
        }

        
        for(int i=1;i<Clause.size();i++)
        {
                    
        a = new Integer(Clause.get(i).get(1));
        b = new Integer(Clause.get(i).get(2));
        
        switch(Clause.get(i).get(0))
            {
                case "=" : temp=a.equals(b);break;
                case "<" : temp=(a<b);break;
                case ">" : temp=(a>b);break;
                case "<=" : temp=(a<=b);break;
                case ">=" : temp=(a>=b);break;
                case "!=" : temp=!a.equals(b);break;
                default : temp=true;
            }
            if(comparison_type==1)
                res= res||temp;
            if(comparison_type==2)
                res= res && temp;
        }
        return res;
    }
    
    String inTable(String col,Vector<String> Tables)
    {
        for(String s: Tables)
        {
            if(metadata.get(s).indexOf(col)>=0)
                   return s;
        }
        return null;
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
