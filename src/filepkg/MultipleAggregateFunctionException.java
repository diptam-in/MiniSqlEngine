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
public class MultipleAggregateFunctionException extends Exception {

    public MultipleAggregateFunctionException() {
        System.out.println("Error: Not More Than One Aggregate Function is Allowed");
        System.exit(0);
    }
    
}
