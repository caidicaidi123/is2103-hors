/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import java.math.BigDecimal;
import javax.ejb.Remote;

/**
 *
 * @author caidi
 */
@Remote
public interface RoomControllerBeanRemote {

    public String createNewRoomType(String typeName, String description, BigDecimal rate);
    
}
