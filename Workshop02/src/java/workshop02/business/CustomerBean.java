/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop02.business;

import java.util.Optional;
import workshop02.model.Customer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
//This is the default behaviour - CMT
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CustomerBean {

	@Resource SessionContext ctx;

	@PersistenceContext private EntityManager em;

	@PostConstruct
	private void init() { }

	@PreDestroy
	private void destroy() { }

	public Customer updateCustomer(Customer customer) {

		Customer updatedCustomer = em.merge(customer);
		return (updatedCustomer);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Customer createCustomer(Customer customer) 
			throws CustomerException {


		Customer c = em.find(Customer.class, customer.getCustomerId());
		if (null != c) {
			throw new CustomerException("Customer id exists");
		}

	
		em.persist(customer);
		
		return (customer);
	}

    public Optional<Customer> findByCustomerId(Integer customerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addNewCustomer(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}