/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Room;
import entity.RoomType;
import error.RoomNotFoundException;
import java.math.BigDecimal;
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
    public void updateRoomById(Long roomId, Long roomNumber, Long roomTypeId, Boolean status) {
        Room room = getRoomById(roomId);

        
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
    
    
    
    
    
    
    
    
    
    
    
    private RoomType getRoomTypeById(Long roomTypeId) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.id=:inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        RoomType roomType = (RoomType) query.getSingleResult();
        return roomType;
    }
    
    private Room getRoomById(Long roomId) {
        Query query = em.createQuery("SELECT rm FROM Room rm WHERE rm.id=:inRoomId");
        query.setParameter("inRoomId", roomId);
        return (Room) query.getSingleResult();
    }
    
    
}
