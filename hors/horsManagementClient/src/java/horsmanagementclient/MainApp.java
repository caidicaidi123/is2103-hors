/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import entity.RoomType;
import java.math.BigDecimal;
import java.util.Scanner;
import session.stateless.RoomControllerBeanRemote;

/**
 *
 * @author caidi
 */
public class MainApp {
    private RoomControllerBeanRemote roomControllerBeanRemote;

    public MainApp() {
    }

    public MainApp(RoomControllerBeanRemote roomControllerBeanRemote) {
        this.roomControllerBeanRemote = roomControllerBeanRemote;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {            
            System.out.println("Welcome to HoRS Management Client!");
            System.out.println("1. Create new room type.");
            System.out.println("10. Exit ");
            response = 0;
            
            while (response < 1 || response > 10) {                
                System.out.print(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    createNewRoomType();
                }
            }
            
            
            
            if (response == 10) {
                break;
            }
        }
    }
    
    
    
    
    private String createNewRoomType() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter room type name: ");
        String typeName = sc.nextLine().trim();
        System.out.println("Please enter room description, including size, bed, capacity etc");
        String description = sc.nextLine().trim();
        System.out.println("Please enter room rate");
        BigDecimal rate = sc.nextBigDecimal();
        
        return roomControllerBeanRemote.createNewRoomType(typeName, description, rate);
    }
}
