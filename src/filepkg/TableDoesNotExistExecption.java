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
public class TableDoesNotExistExecption extends Exception {

    public TableDoesNotExistExecption(String get) {
        System.out.println("Error: Table Does Not Exist Exception: Table "+ get);
        System.exit(0);
    }
    
}
