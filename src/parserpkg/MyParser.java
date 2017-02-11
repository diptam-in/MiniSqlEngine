/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserpkg;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import java.util.Vector;

/**
 *
 * @author diptam
 */
public class MyParser {
    
    private String query;
    private EDbVendor vendor;
    private TGSqlParser sqlparser;
    private TCustomSqlStatement stmt;
    
    public MyParser()
    {
        vendor = EDbVendor.dbvmysql;
        sqlparser = new TGSqlParser(vendor);
    }
    public boolean setQuery(String text)
    {
        sqlparser.sqltext = text;
        int flag = sqlparser.parse();
        if(flag==0)
        {
            if(sqlparser.sqlstatements.size()>0)
            {
                stmt = sqlparser.sqlstatements.get(0);
                if(stmt.sqlstatementtype == ESqlStatementType.sstselect)
                   return true;
                return false;
            }
            return false;
        }
        return false;

    }
    
    public Vector<String> getListOfColumns()
    {
        Vector<String> res = new Vector();
        TSelectSqlStatement pStmt = (TSelectSqlStatement)stmt;
        for(int i=0; i < pStmt.getResultColumnList().size();i++){
            TResultColumn resultColumn = pStmt.getResultColumnList().getResultColumn(i);
            res.add(resultColumn.getExpr().toString());
        }
        return res;
    }
    
    public Vector<String> getListOfTables()
    {
        Vector<String> res = new Vector();
        TSelectSqlStatement pStmt = (TSelectSqlStatement)stmt;
        for(int i = 0 ; i< pStmt.joins.size();i++)
        {
            TJoin sub = pStmt.joins.getJoin(i);
            res.add(sub.getTable().toString());
        }
        return res;
    }
    
    public String getClause()
    {
        TSelectSqlStatement pStmt = (TSelectSqlStatement)stmt;
        if(pStmt.getWhereClause()!=null)
            return pStmt.getWhereClause().toString().substring(6);
        return null;
    }
    
}
