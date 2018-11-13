package workshop02.web;

import workshop02.model.PurchaseOrder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

@WebServlet(urlPatterns = { "/purchaseOrder" })
public class PurchaseOrderServlet extends HttpServlet {
    
    //Get a reference to Entity Manager
    @PersistenceContext private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Integer custId = Integer.parseInt(req.getParameter("custId"));
        
        //Construct a named query
        TypedQuery<PurchaseOrder> query = em.createNamedQuery(
            "PurchaseOrder.findByCustomerId", PurchaseOrder.class);
        //Set the parameter :custId
        query.setParameter("custId", custId);
        
        List<PurchaseOrder> result = query.getResultList();
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (PurchaseOrder po: result) {
            builder.add(po.toJson());
        }
        
        TypedQuery<Integer> query2 = em.createNamedQuery(
                "Integer.countPurchaseOrderByCustomer", Integer.class);
        query2.setParameter("custId", custId);
        List<Integer> result2 = query2.getResultList();
        
        System.out.println(">>> count: " + result2);
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter pw = resp.getWriter()) {
            pw.print(builder.build().toString());
        }   
    }    
}


