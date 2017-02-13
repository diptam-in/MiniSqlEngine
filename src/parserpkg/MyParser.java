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
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javafx.util.Pair;

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
        else
        {
            System.out.println(sqlparser.getErrormessage());
            return false;
        }
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
    
    public int getClauseType()
    {
        TSelectSqlStatement pStmt = (TSelectSqlStatement)stmt;
        if(pStmt.getWhereClause()!=null)
        {
            switch(pStmt.getWhereClause().getCondition().getExpressionType())
            {
                case simple_comparison_t: return 0;
                case logical_or_t: return 1;
                case logical_and_t: return 2;
            }
        }
        return -1;
    }
    
    public Vector< Pair<String,Pair<String,String> > > getConditionList()
    {
        TSelectSqlStatement pStmt = (TSelectSqlStatement)stmt;
        Vector< Pair<String,Pair<String,String> > > map = new Vector();
        
        if(pStmt.getWhereClause()==null) return map;
        TExpression wexpr= pStmt.getWhereClause().getCondition();
        
        Pair<String,Pair<String,String> > expr;
        Pair<String,String> operand;
        
        if(this.getClauseType()>0)
        {
            wexpr = pStmt.getWhereClause().getCondition().getLeftOperand();
            operand = new Pair(wexpr.getLeftOperand().toString(),wexpr.getRightOperand().toString());
            expr = new Pair(wexpr.getComparisonOperator().toString(),operand);
            map.add(expr);   
            wexpr = pStmt.getWhereClause().getCondition().getRightOperand();
            operand = new Pair(wexpr.getLeftOperand().toString(),wexpr.getRightOperand().toString());
            expr = new Pair(wexpr.getComparisonOperator().toString(),operand);
            map.add(expr);    
        }
        else
        {
            operand = new Pair(wexpr.getLeftOperand().toString(),wexpr.getRightOperand().toString());
            expr = new Pair(wexpr.getComparisonOperator().toString(),operand);
            map.add(expr);
        }
        return map;
    }
    
}
