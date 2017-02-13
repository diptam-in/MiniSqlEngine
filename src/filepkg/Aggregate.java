/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filepkg;

import java.util.Vector;

/**
 *
 * @author diptam
 */
public class Aggregate {
    
    private String path = "/home/diptam/DBS/Assignment-1/";
    Vector<Vector<String> > getResult(String func_type, int Col_num,Vector<Vector<String> > data)
    {
        switch(func_type){
            case "Avg": return getAvg(Col_num,data);
            case "Max": return getMax(Col_num,data);
            case "Min": return getMin(Col_num,data);
            case "Sum": return getSum(Col_num,data);
            default: return getDistinct(Col_num,data);
        } 
    }
    Vector<Vector<String> > getSum(int Col_num,Vector<Vector<String> > data)
    {
        int size =data.size();
        int sum=0;
        Integer s;
        for(int i=0;i<size;i++)
        {
            s = new Integer(data.get(i).get(Col_num));
            sum+=s;
        }
        for(int i=0;i<size-1;i++)
        {
            data.remove(0);
        }
        data.get(0).remove(Col_num);
        data.get(0).add(Col_num,new Integer(sum).toString());
        return data;
    }
    
    Vector<Vector<String> > getMin(int Col_num,Vector<Vector<String> > data)
    {
        int size =data.size();
        Integer max = new Integer(data.get(0).get(Col_num)); 
        Integer pos = new Integer(0);
        Integer s;
        for(int i=1;i<size;i++)
        {
            s = new Integer(data.get(i).get(Col_num));
            if(s<max)
            {
                max = s;
                pos = i;
            }
        }
        Vector<Vector<String> > res = new Vector();
        res.add(data.get(pos));
        
        return res;
    }
    
    Vector<Vector<String> > getMax(int Col_num,Vector<Vector<String> > data)
    {
        int size =data.size();
        Integer max = new Integer(data.get(0).get(Col_num)); 
        Integer pos = new Integer(0);
        Integer s;
        for(int i=1;i<size;i++)
        {
            s = new Integer(data.get(i).get(Col_num));
            if(s>max)
            {
                max = s;
                pos = i;
            }
        }
        Vector<Vector<String> > res = new Vector();
        res.add(data.get(pos));
        
        return res;
    }
    
    Vector<Vector<String> > getAvg(int Col_num,Vector<Vector<String> > data)
    {
        int size =data.size();
        int sum=0;
        Integer s;
        for(int i=0;i<size;i++)
        {
            s = new Integer(data.get(i).get(Col_num));
            sum+=s;
        }
        sum=sum/size;
        for(int i=0;i<size-1;i++)
        {
            data.remove(0);
        }
        data.get(0).remove(Col_num);
        data.get(0).add(Col_num,new Integer(sum).toString());
        return data;
    }
    
    Vector<Vector<String> > getDistinct(int Col_num,Vector<Vector<String> > data)
    {
        System.out.println(data);
        boolean[] arr = new boolean[data.size()];
        for(int i=0;i<data.size();i++)
        {
            if(arr[i]==true) continue;
            for(int j=i+1;j<data.size();j++)
            {
               if(data.get(i).get(Col_num).equals(data.get(j).get(Col_num)))
                   arr[j] = true;
            }
        }
        Vector<Vector<String> > result = new Vector();
        for(int i=0;i<data.size();i++)
        {
            if(!arr[i])
                result.add(data.get(i));
        }
        return result;
    }
}
