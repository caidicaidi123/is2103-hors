/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import javax.persistence.NoResultException;

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

    public void updateRoomById(Long roomId, Long roomNumber, Long roomTypeId, Boolean inUse) throws NoResultException;

    public List<Room> retrieveAllRooms();

    public void deleteRoom(Long roomId);

    public List<RoomRate> retrieveAllRoomRates();

    public RoomRate getRoomRateById(Long roomRateId);

    public void updateRoomRate(Long roomRateId, String roomRateName, String description, BigDecimal rate);

    public void createNewRoomRate(String roomRateName, String descripition, BigDecimal rate);
    
    
}
