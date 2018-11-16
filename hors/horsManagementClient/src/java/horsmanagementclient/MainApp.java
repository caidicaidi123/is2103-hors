/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import entity.Room;
import entity.RoomType;
import error.NoResultException;
import error.NoRoomTypeException;
import java.math.BigDecimal;
import java.util.List;
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
            System.out.println("********");
            System.out.println("Welcome to HoRS Management Client!");
            System.out.println("1. Create new room type.");
            System.out.println("2. View Room Type Details.");
            System.out.println("3. Update Room Type.");
            System.out.println("4. View All Room Types");
            System.out.println("5. Create new room");
            System.out.println("6. Update Room");
            System.out.println("7. View All Rooms");
            System.out.println("10. Exit ");
            response = 0;
            
            while (response < 1 || response > 10) {                
                System.out.print(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    createNewRoomType();
                } 
                else if (response == 2) {
                    viewRoomTypeDetails();
                }
                else if (response == 3) {
                    updateRoomType();
                }
                else if (response == 4) {
                    viewAllRoomTypes();
                }
                else if (response == 5) {
                    createNewRoom();
                }
                else if (response == 6) {
                    updateRoom();
                }
                else if (response == 7) {
                    viewAllRooms();
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            
            
            if (response == 10) {
                break;
            }
        }
    }
    
    
    
    
    private String createNewRoomType() {
        System.out.println("********");
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter room type name: ");
        String typeName = sc.nextLine().trim();
        System.out.println("Please enter room description, including size, bed, capacity etc");
        String description = sc.nextLine().trim();
        
        return roomControllerBeanRemote.createNewRoomType(typeName, description);
    }
    
    private void viewRoomTypeDetails() {
        System.out.println("********");
        List<RoomType> roomTypes = roomControllerBeanRemote.retrieveAllRoomType();
        Scanner sc = new Scanner(System.in);
        
        for (RoomType roomType : roomTypes) {
            String title = roomType.getId().toString() + " " + roomType.getTypeName();
            System.out.println(title);
        }
           
        System.out.println("Select a room type to view detials");
        System.out.print(">");
        Long roomTypeId = sc.nextLong();
        try {
            RoomType roomType = roomControllerBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.println("Room type: "+roomType.getTypeName()+"\n"+"Room type details: "+roomType.getDescription());
        } catch (Exception e) {
            System.out.println("Room Type does not exist!");
        }
    }
    
    private void updateRoomType() {
        System.out.println("********");
        
        // retrieve and display all room types
        List<RoomType> roomTypes = roomControllerBeanRemote.retrieveAllRoomType();
        Scanner sc = new Scanner(System.in);
        
        for (RoomType roomType : roomTypes) {
            String title = roomType.getId().toString() + " " + roomType.getTypeName();
            System.out.println(title);
        }
        
        // let user select a room type and update
        System.out.println("Select a room type to update");
        System.out.print(">");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        try {
            RoomType roomType = roomControllerBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.println("Room type: "+roomType.getTypeName()+"\n"+"Room type details: "+roomType.getDescription());
            System.out.println("********");
        } catch (NoResultException e) {
            System.out.println("Room Type does not exist!");
        }
        
        System.out.println("Please enter new room type name");
        System.out.print(">");
        String typeName = sc.nextLine().trim();
        
        System.out.println("Please enter new description");
        System.out.print(">");
        String description = sc.nextLine().trim();
        
        
        roomControllerBeanRemote.updateRoomTypeById(roomTypeId, typeName, description);
    }
    
    private void viewAllRoomTypes() {
        System.out.println("********");
        List<RoomType> roomTypes = roomControllerBeanRemote.retrieveAllRoomType();
        for (RoomType roomType : roomTypes) {
            String title = roomType.getId().toString() + " " + roomType.getTypeName();
            System.out.println(title);
        }
    }
    
    private void createNewRoom() {
        Long roomNumber, roomTypeId;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("********");
        System.out.println("Please enter room number");
        System.out.print(">");
        roomNumber = sc.nextLong();
        sc.nextLine();
        
        
        
        // retrieve room types
        List<RoomType> roomTypes = roomControllerBeanRemote.retrieveAllRoomType();
        
        
        
        
        if (roomTypes.size() > 0) {
            System.out.println("Please select a room type");
            System.out.println("0. Do Not Set Room Type Now");
            for (RoomType roomType : roomTypes) {
                String title = roomType.getId().toString() + " " + roomType.getTypeName();
                System.out.println(title);
            }
            System.out.print(">");
            roomTypeId = sc.nextLong();
            sc.nextLine();
        } else {
            roomTypeId = 0L;
        }
        
        
        roomControllerBeanRemote.createNewRoom(roomNumber, roomTypeId);
        System.out.println("Room: "+roomNumber+" created successfully");
    }
    
    private void updateRoom() {
        viewAllRooms();
        System.out.println("Please enter room ID:");
        System.out.print(">");
        
    }
    
    private void viewAllRooms() {
        System.out.println("********");
        List<Room> rooms = roomControllerBeanRemote.retrieveAllRooms();
        for(Room room:rooms) {
            String result;
            result = "Room id: "+room.getId()+" Room Number: "+ room.getRoomNumber() + " Room in use: " + room.getInUse();
            try {
                result = result + " Room Type: " + room.getRoomType().getTypeName();
            } catch (Exception e) {
                result = result + " Room Type: No Room Type Assigned Yet";
            }
            System.out.println(result);
        }
    }
    
}
