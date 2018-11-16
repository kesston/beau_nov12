/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop04.rest;

import workshop04.business.CustomerBean;
import workshop04.model.Customer;
import java.sql.SQLException;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

public class FindByCustomerIdRunnable implements Runnable {
    
    private Integer custId;
    private CustomerBean customerBean;    
    private AsyncResponse asyncResp;

    public FindByCustomerIdRunnable(Integer cid,
            CustomerBean cb, AsyncResponse ar) {
        custId = cid;
        customerBean = cb;
        asyncResp = ar;
    }
        

    @Override
    public void run() {
        System.out.println(">>> running FindByCustomerIdRunnable");
        Optional<Customer> opt = null;
        try {
            opt = customerBean.findByCustomerId(custId);
        } catch (SQLException ex) {
            // { "error": "error message" }
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            // 500 Server Error 
            
            asyncResp.resume(Response.serverError().entity(error).build());
            return;
        }

        //Return 404 Not Found if the customer does not exists
        if (!opt.isPresent()) {
            asyncResp.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        //Return the data as JSON
        asyncResp.resume(Response.ok(opt.get().toJson()).build());
        
        System.out.println(">>> resuming request");

    }
}