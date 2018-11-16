/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.RoomType;
import error.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author caidi
 */
@Remote
public interface RoomControllerBeanRemote {

    public String createNewRoomType(String typeName, String description);

    public List<RoomType> retrieveAllRoomType();

    public RoomType retrieveRoomTypeById(Long roomTypeId) throws NoResultException;

    public void updateRoomTypeById(Long roomTypeId, String typeName, String description);

    public void createNewRoom(Long roomNumber, Long roomTypeId);
    
    
}
