/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import entity.Employee;
import entity.Room;
import entity.RoomType;
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
            initialiseEmployeeData();
        }
        
        if (em.find(RoomType.class, 1l) == null && em.find(Room.class, 1l) == null) {
            initialiseRoomTypeData();
        }
    }
    
    private void initialiseEmployeeData() {
        Employee employee = new Employee("Test Manager");
        em.persist(employee);
    }
    
    private void initialiseRoomTypeData() {
        RoomType roomType;
        Room room;
        
        roomType = new RoomType("Deluxe Room", "Deluxe Room Description");
        em.persist(roomType);
        for(Long roomNumber=101L; roomNumber<106L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }

        
        
        roomType = new RoomType("Premier Room", "Premier Room Description");
        em.persist(roomType);
        for(Long roomNumber=201L; roomNumber<206L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Family Room", "Family Room Description");
        em.persist(roomType);
        for(Long roomNumber=301L; roomNumber<306L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Junior Suite", "Junior Suite Description");
        em.persist(roomType);
        for(Long roomNumber=401L; roomNumber<406L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Grand Suite", "Grand Suite Description");
        em.persist(roomType);
        for(Long roomNumber=401L; roomNumber<406L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
    }
}
