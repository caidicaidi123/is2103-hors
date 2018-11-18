/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Employee;
import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author caidi
 */
@Stateless
public class AccountControllerBean implements AccountControllerBeanRemote {

    @PersistenceContext(unitName = "hors-ejbPU")
    private EntityManager em;

    @Override
    public boolean employeeAuthentication(String accountName, String password) throws NoResultException{
        Employee employee;
        Query query = em.createQuery("SELECT acc FROM Employee acc WHERE acc.accountName=:inAccountName");
        query.setParameter("inAccountName", accountName);
        employee = (Employee) query.getSingleResult();
        if (employee == null) {
            throw new NoResultException("Account not found!");
            
        } else if (employee.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean partnerAuthentication(String accountName, String password) throws NoResultException {
        Partner partner;
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.partnerAccountName=:inAccountName");
        query.setParameter("inAccountName", accountName);
        partner = (Partner) query.getSingleResult();
        
        if (partner == null) {
            throw new NoResultException("Account not found!");
            
        } else if (partner.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void createNewEmployee(String accountName, String password) {
        Employee employee = new Employee(accountName, password);
        em.persist(employee);
    }
    
    @Override
    public void createNewPartner(String accountName, String password) {
        Partner partner = new Partner(accountName, password);
        em.persist(partner);
    }
    
    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }
    
    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");
        return query.getResultList();
    }
}
