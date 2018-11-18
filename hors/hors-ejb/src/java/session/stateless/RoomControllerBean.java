/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
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
public class RoomControllerBean implements RoomControllerBeanRemote {

    @PersistenceContext(unitName = "hors-ejbPU")
    private EntityManager em;

    @Override
    public String createNewRoomType(String typeName, String description) {
        RoomType roomType = new RoomType(typeName, description);
        try {
            em.persist(roomType);
            return "New room type created successfully";
        } catch (Exception e) {
            return e.toString();
        }
    }
    
    @Override
    public List<RoomType> retrieveAllRoomType() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        return query.getResultList();
    }
    
    @Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) throws NoResultException{
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.id=:inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        RoomType roomType = (RoomType) query.getSingleResult();
        if (roomType == null) {
            throw new NoResultException("Room type does not exist!");
        }
        return roomType;
    }
    
    
    @Override
    public void updateRoomTypeById(Long roomTypeId, String typeName, String description) {
        RoomType roomType = getRoomTypeById(roomTypeId);
        roomType.setTypeName(typeName);
        roomType.setDescription(description);
    }
    
    /**
     *
     * @param roomNumber
     * @param roomTypeId
     */
    @Override
    public void createNewRoom(Long roomNumber, Long roomTypeId) {
        Room room;
        // room type not set
        if (roomTypeId == 0) {
            room = new Room(roomNumber);
        } else {
            RoomType roomType = getRoomTypeById(roomTypeId);
            room = new Room(roomNumber, roomType);
            roomType.getRooms().add(room);
        }
        
        em.persist(room);
    }
    
    @Override
    public void updateRoomById(Long roomId, Long roomNumber, Long roomTypeId, Boolean status) throws NoResultException{
        Room room = getRoomById(roomId);
        
        if (room == null) {
            throw new NoResultException("Room not found!");
        }
        
        room.setRoomNumber(roomNumber);
        if (roomTypeId != 0) {
            RoomType roomType = getRoomTypeById(roomTypeId);
            room.setRoomType(roomType);
        }
        room.setStatus(status);           


    }
    
    @Override
    public List<Room> retrieveAllRooms() {
        
        Query query = em.createQuery("SELECT rms FROM Room rms");
        return query.getResultList();
    }
    
    @Override
    public void deleteRoom(Long roomId) throws NoResultException {
        Room room = getRoomById(roomId);
        
        if (room == null) {
            throw new NoResultException("Room not found!");
        }        
        
        // if room status is not available (false) 
        if (room.getStatus() == false) {
            room.setIsDisabled(Boolean.TRUE);
        } else {
            // remove room from roomType.rooms
            RoomType roomType = room.getRoomType();
            if (roomType != null) {
                roomType.getRooms().remove(room);
            }
            // remove room entity
            em.remove(room);
        }
    }
    
    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr from RoomRate rr");
        return query.getResultList();
    }
    
    @Override
    public RoomRate getRoomRateById(Long roomRateId) {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.id=:inRoomRateId");
        query.setParameter("inRoomRateId", roomRateId);
        RoomRate roomRate = (RoomRate) query.getSingleResult();
        
        // !!!!!! initialise lazy relationship
        roomRate.getRoomTypes().size();
        return roomRate;
    }
    
    @Override
    public void updateRoomRate(Long roomRateId, String roomRateName, String description, BigDecimal rate) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        roomRate.setRoomRateName(roomRateName);
        roomRate.setDescription(description);
        roomRate.setRate(rate);
    } 
    
    @Override
    public void createNewRoomRate(String roomRateName, String descripition, BigDecimal rate) {
        RoomRate roomRate = new RoomRate(roomRateName, descripition, rate);
        em.persist(roomRate);
    }
    
    
    @Override
    public void deleteRoomRateById(Long roomRateId) {
        RoomRate roomRate = getRoomRateById(roomRateId);
        
        if (roomRate.getRoomTypes().isEmpty()) {
            em.remove(roomRate);
        } else {
            roomRate.setIsDisabled(Boolean.TRUE);
        }
    }
    
    @Override
    public List<Room> retrieveAllAvailiableRooms() {
        Query query = em.createQuery("SELECT rm FROM Room rm WHERE rm.status=:inStatus AND rm.isDisabled=:inIsDisabled");
        query.setParameter("inStatus", true);
        query.setParameter("inIsDisabled", false);
        return query.getResultList();
    }
    
    @Override
    public void createNewReservation(String guessName, Date checkInDate, Date checkOutDate, Long roomId) throws NoResultException{
        Room room = em.find(Room.class, roomId);
        if (room == null) {
            throw new NoResultException("Room not found!");
        }
        Reservation reservation = new Reservation(guessName, checkInDate, checkOutDate, roomId);
        em.persist(reservation);
    }
    
    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        return query.getResultList();
    }
    
    @Override
    public Room getRoomById(Long roomId) throws NoResultException {
        Query query = em.createQuery("SELECT rm FROM Room rm WHERE rm.id=:inRoomId");
        query.setParameter("inRoomId", roomId);
        Room room =  (Room) query.getSingleResult();
        return room;
    }
    
    @Override
    public String processReservations() {
        List<Reservation> reservations;
        String report = "";
        Query query = em.createQuery("SELECT r FROM Reservation r");
        reservations =  query.getResultList();
        
        for (Reservation reservation : reservations) {
            Room room = em.find(Room.class, reservation.getRoomId());
            if (room == null) {
                report = report + "Room ID: "+reservation.getRoomId().toString()+" is not found \n";
                continue;
            } 
            else if(!room.getStatus()) {
                report = report + "Room ID: "+reservation.getRoomId().toString()+" is not available \n";
                continue;
            } 
            else if(room.getIsDisabled()) {
                report = report + "Room ID: "+reservation.getRoomId().toString()+" is disabled \n";
                continue;
            }
            else {
                room.setStatus(Boolean.FALSE);
                room.setGuestName(reservation.getGuestName());
                em.remove(reservation);
                report = report + "Reservation ID: "+reservation.getId().toString()+" is processed successfully \n";
            }
        }
        
        return report;
    }
    
    @Override
    public String checkInGuest(String guestName) throws NoResultException{
        Room room;
        String report = "";
        
        
        Query query = em.createQuery("SELECT rm FROM Room rm WHERE rm.guestName=:inGuestName");
        query.setParameter("inGuestName", guestName);
        room = (Room) query.getSingleResult();
        
        if (room == null) {
            throw new NoResultException("Reservation not found");
        }
        else if (room.getIsCheckedIn()) {
            throw new NoResultException("Reservation not found");
        }
        else {
            room.setGuestName(guestName);
            room.setIsCheckedIn(Boolean.TRUE);
            report = report + "Check in successfully\nPlease proceed to Room: "+room.getRoomNumber().toString();
        }
        return report;
    }
    
    @Override
    public String checkOutGuest(String guestName) throws NoResultException{
        Room room;
        String report = "";
        
        
        Query query = em.createQuery("SELECT rm FROM Room rm WHERE rm.guestName=:inGuestName");
        query.setParameter("inGuestName", guestName);
        room = (Room) query.getSingleResult();
        
        if (room == null) {
            throw new NoResultException("Room not checked in");
        }
        else if (!room.getIsCheckedIn()) {
            throw new NoResultException("Room not checked in");
        }
        else {
            room.setGuestName(null);
            room.setIsCheckedIn(Boolean.FALSE);
            room.setStatus(Boolean.TRUE);
            report = report + "Check out successfully\nRoom Number: "+room.getRoomNumber().toString();
        }
        return report;
    }
    
    
    
    
    
    private RoomType getRoomTypeById(Long roomTypeId) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.id=:inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        RoomType roomType = (RoomType) query.getSingleResult();
        return roomType;
    }
    

    
    
}
