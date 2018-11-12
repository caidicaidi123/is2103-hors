/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.RoomType;
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
    public String createNewRoomType(String typeName, String description, BigDecimal rate) {
        RoomType roomType = new RoomType(typeName, description, rate);
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
}
