/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import javax.ejb.EJB;
import session.stateless.AccountControllerBeanRemote;
import session.stateless.RoomControllerBeanRemote;

/**
 *
 * @author caidi
 */
public class Main {

    @EJB(name = "AccountControllerBeanRemote")
    private static AccountControllerBeanRemote accountControllerBeanRemote;

    @EJB(name = "RoomControllerBeanRemote")
    private static RoomControllerBeanRemote roomControllerBeanRemote;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(roomControllerBeanRemote, accountControllerBeanRemote);
        mainApp.landingPate();
    }
    
}
