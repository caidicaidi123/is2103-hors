/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Employee;
import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import javax.persistence.NoResultException;

/**
 *
 * @author caidi
 */
@Remote
public interface AccountControllerBeanRemote {

    public boolean employeeAuthentication(String accountName, String password) throws NoResultException;

    public boolean partnerAuthentication(String accountName, String password) throws NoResultException;

    public void createNewEmployee(String accountName, String password);

    public void createNewPartner(String accountName, String password);

    public List<Employee> retrieveAllEmployees();

    public List<Partner> retrieveAllPartners();

    public boolean guestAuthentication(String email, String password) throws NoResultException;

    public void createNewGuest(String email, String password);
    
}
