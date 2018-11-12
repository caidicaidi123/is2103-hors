/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import entity.Employee;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author caidi
 */
@Singleton
@LocalBean
@Startup
public class DataInitialisationBean {
    @PersistenceContext(unitName = "hors-ejbPU")
    private EntityManager em;

    public DataInitialisationBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        if (em.find(Employee.class, 1l) == null) {
            initialiseData();
        }
    }
    
    private void initialiseData() {
        Employee employee = new Employee("Test Manager");
        em.persist(employee);
    }
}
