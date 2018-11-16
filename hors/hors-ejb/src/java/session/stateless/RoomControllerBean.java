/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Room;
import entity.RoomType;
import error.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
        RoomType roomType = getRoomType(roomTypeId);
        roomType.setTypeName(typeName);
        roomType.setDescription(description);
    }
    
    @Override
    public void createNewRoom (Long roomNumber, Long roomTypeId) {
        RoomType roomType = getRoomType(roomTypeId);
        Room room = new Room(roomNumber, roomType);
    }
    
    private RoomType getRoomType(Long roomTypeId) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.id=:inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        RoomType roomType = (RoomType) query.getSingleResult();
        return roomType;
    }
}
