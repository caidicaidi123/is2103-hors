/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import entity.Employee;
import entity.Partner;
import entity.RateType;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        
        if (em.find(Partner.class, 1l) == null) {
            initialisePartnerData();
        }
         
        if (em.find(RoomRate.class, 1l) == null) {
            initialiseRoomRateData();
        }
        
        if (em.find(RoomType.class, 1l) == null && em.find(Room.class, 1l) == null) {
            try {
                initialiseRoomTypeData();
                assignRoomRateToRoomType();
                
            } catch (NoResultException e) {
                System.out.println("#### Data initialisation error!");
            }
        }
        
        
      
        
    }
    
    
    private void initialisePartnerData() {
        Partner partner;
        partner = new Partner("partner1", "password");
        em.persist(partner);
        
        partner = new Partner("partner2", "password");
        em.persist(partner);
        
    }
    
    private void initialiseEmployeeData() {
        Employee employee;
        employee = new Employee("alice@gmail.com", "password");
        em.persist(employee);
        
        employee = new Employee("tom@gmail.com", "password");
        em.persist(employee);
        
        employee = new Employee("bob@gmail.com", "password");
        em.persist(employee);
    }
    
    private void initialiseRoomTypeData() throws NoResultException {
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
        // pre-load 5 rooms of newly created room type
        for(Long roomNumber=201L; roomNumber<206L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Family Room", "Family Room Description");
        em.persist(roomType);
        // pre-load 5 rooms of newly created room type
        for(Long roomNumber=301L; roomNumber<306L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Junior Suite", "Junior Suite Description");
        em.persist(roomType);
        // pre-load 5 rooms of newly created room type
        for(Long roomNumber=401L; roomNumber<406L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
        roomType = new RoomType("Grand Suite", "Grand Suite Description");
        em.persist(roomType);
        // pre-load 5 rooms of newly created room type
        for(Long roomNumber=401L; roomNumber<406L; roomNumber++){
            room = new Room(roomNumber, roomType);
            em.persist(room);
        }
        
    }
    
    private void initialiseRoomRateData() {
        String roomRateName, description;
        BigDecimal rate;
        RoomRate roomRate;
        
        // published rate
        roomRateName = "Published Rate";
        description = "This is the base rate per night for a room type that is offered to walk- in reservation for same day check-in.";
        rate = new BigDecimal(250.00);
        roomRate = new RoomRate(roomRateName, description, rate);
        em.persist(roomRate);
        
        // normal rate
        roomRateName = "Normal Rate";
        description = "This is the normal rate per night for a room type that is offered to online reservation via HoRS, including external partners. This is the default rate per night for any room type offered to online reservation";
        rate = new BigDecimal(200.00);
        roomRate = new RoomRate(roomRateName, description, rate);
        em.persist(roomRate);
        
        // peak rate
        roomRateName = "Peak Rate";
        description = "This is the normal rate per night for a room type that is offered to online reservation via HoRS, including external partners. This is the default rate per night for any room type offered to online reservation";
        rate = new BigDecimal(300.00);
        roomRate = new RoomRate(roomRateName, description, rate);
        em.persist(roomRate);
        
        // promotion rate
        roomRateName = "promotion Rate";
        description = "This is the discounted rate per night for a room type that is offered to online reservation via HoRS, including to external partners, during predefined promotional periods.";
        rate = new BigDecimal(150.00);
        roomRate = new RoomRate(roomRateName, description, rate);
        em.persist(roomRate);
        
    }   
    
    private void assignRoomRateToRoomType() {
        RoomRate publishedRate, normalRate;
        List<RoomType> roomTypes;
        
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomRateName=:inRoomRateName");
        query.setParameter("inRoomRateName", "Published Rate");
        publishedRate = (RoomRate) query.getSingleResult();
        
        query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomRateName=:inRoomRateName");
        query.setParameter("inRoomRateName", "Normal Rate");
        normalRate = (RoomRate) query.getSingleResult();
        
        
        
        
        
        query = em.createQuery("SELECT rt FROM RoomType rt");
        roomTypes = query.getResultList();
        if (roomTypes == null) {
            throw new NoResultException("Room types are not initialised!");
        }
        
        for(RoomType roomType:roomTypes) {
            roomType.getRoomRates().add(publishedRate);
            roomType.getRoomRates().add(normalRate);
            publishedRate.getRoomTypes().add(roomType);
            normalRate.getRoomTypes().add(roomType);
        }
    }
}
