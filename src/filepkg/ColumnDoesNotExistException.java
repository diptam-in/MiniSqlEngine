/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filepkg;

/**
 *
 * @author diptam
 */
public class ColumnDoesNotExistException extends Exception {

    public ColumnDoesNotExistException(String s) {
        System.out.println("Error: Column does not exist for these tables: Column name: "+s );
        System.exit(0);
    }
    
}
