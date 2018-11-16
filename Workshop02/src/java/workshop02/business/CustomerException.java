/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop02.business;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerException extends Exception {

    public CustomerException() {
        super();
    }
    
    public CustomerException(String msg) {
        super(msg);
    }    
}
