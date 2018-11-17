/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
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
        Integer response = -1;
        
        while (true) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("************************");
            System.out.println("Welcome to HoRS Management Client!");
            System.out.println("Main Menu:");
            System.out.println("0. Exit ");
            System.out.println("************************");
            System.out.println("1. Create new room type.");
            System.out.println("2. View Room Type Details.");
            System.out.println("3. Update Room Type.");
            System.out.println("4. View All Room Types");
            System.out.println("5. Create new room");
            System.out.println("6. Update Room");
            System.out.println("7. View All Rooms");
            System.out.println("8. Delete a Room");
            System.out.println("9. Create New Room Rate");
            System.out.println("10. View All Room Rates");
            System.out.println("11. View a Room Rate Details");
            System.out.println("12. Update a Room Rate");
            
            // clear response from last iteration
            response = -1;
            
            while (response < 0 || response > 20) {                
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
                else if (response == 8) {
                    deleteRoom();
                }
                else if (response == 9) {
                    createNewRoomRate();
                }
                else if (response == 10) {
                    viewAllRoomRates();
                }
                else if (response == 11) {
                    viewRoomRateDetails();
                }
                else if (response == 12) {
                    updateRoomRate();
                }
                else if (response == 0) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            
            // exist program
            if (response == 0) {
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
        
        return roomControllerBeanRemote.createNewRoomType(typeName, description);
    }
    
    private void viewRoomTypeDetails() {
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
            List<RoomRate> roomRates = roomType.getRoomRates();
            for (RoomRate roomRate : roomRates) {
                System.out.println("Room Rate: "+roomRate.getId().toString()+" Room Rate Name: "+roomRate.getRoomRateName() + " Rate: $" + roomRate.getRate().toString());
            }
            
        } catch (Exception e) {
            System.out.println("Room Type does not exist!");
        }
    }
    
    private void updateRoomType() {        
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
            System.out.println("************************");
        } catch (Exception e) {
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
        List<RoomType> roomTypes = roomControllerBeanRemote.retrieveAllRoomType();
        for (RoomType roomType : roomTypes) {
            String title = roomType.getId().toString() + " " + roomType.getTypeName();
            System.out.println(title);
        }
    }
    
    private void createNewRoom() {
        Long roomNumber, roomTypeId;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("************************");
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
                String title = roomType.getId().toString() + ". " + roomType.getTypeName();
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
        Scanner sc = new Scanner(System.in);
        Long roomId, roomTypeId, roomNumber;
        Integer statusId = 0;
        Boolean status;
        
        // display all rooms
        viewAllRooms();
        
        // user input: room id
        System.out.println("Please enter room ID:");
        System.out.print(">");
        roomId = sc.nextLong();
        sc.nextLine();
        
        // user input: room number
        System.out.println("Please enter new room number: ");
        System.out.print(">");
        roomNumber = sc.nextLong();
        sc.nextLine();
        
        // user input: room status
        while (statusId != 1 && statusId != 2) {
            System.out.println("Please update room status: \n1. Available \n2. Not Available");
            System.out.print(">");
            statusId = sc.nextInt();
            sc.nextLine();
        }
        
        status = statusId == 1;
        
        // user input: room type id
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
        
        try {
            roomControllerBeanRemote.updateRoomById(roomId, roomNumber, roomTypeId, status);
        } catch (Exception e) {
            System.out.println("Room not found!");
        }
        
    }
    
    private void viewAllRooms() {
        System.out.println("************************");
        List<Room> rooms = roomControllerBeanRemote.retrieveAllRooms();
        for(Room room:rooms) {
            String result;
            result = "Room id: "+room.getId()+" Room Number: "+ room.getRoomNumber() + " Room available: " + room.getStatus() + " Room is disabled: " + room.getIsDisabled();
            try {
                result = result + " Room Type: " + room.getRoomType().getTypeName();
            } catch (Exception e) {
                result = result + " Room Type: No Room Type Assigned Yet";
            }
            System.out.println(result);
        }
    }
    
    private void deleteRoom() {
        Scanner sc = new Scanner(System.in);
        Long roomId;
        viewAllRooms();
        
        // user input: room id
        System.out.println("Please enter room ID:");
        System.out.print(">");
        roomId = sc.nextLong();
        sc.nextLine();
        
        try {
            roomControllerBeanRemote.deleteRoom(roomId);
        } catch (Exception e) {
            System.out.println("Room not found!");
        }
    }
    
    private void viewAllRoomRates() {
        List<RoomRate> roomRates = roomControllerBeanRemote.retrieveAllRoomRates();
        for (RoomRate roomRate : roomRates) {
            System.out.println("Room Rate ID: "+roomRate.getId() + " Room Rate Name: "+roomRate.getRoomRateName());
        }
    }
    
    private Long viewRoomRateDetails() {
        Scanner sc = new Scanner(System.in);
        Long roomRateId;
        viewAllRoomRates();
        
        System.out.println("Please Enter Room Rate ID:");
        System.out.print(">");
        roomRateId = sc.nextLong();
        sc.nextLine();
        
        RoomRate roomRate = roomControllerBeanRemote.getRoomRateById(roomRateId);
        System.out.println("Room Rate ID: "+roomRate.getId());
        System.out.println("Room Rate Name: " + roomRate.getRoomRateName());
        System.out.println("Room Rate Description: " + roomRate.getDescription());
        System.out.println("Room Rate Per Night: $" + roomRate.getRate());
        
        List<RoomType> roomTypes = roomRate.getRoomTypes();
        if (!roomTypes.isEmpty()) {
            System.out.println("Room Types Adopting This Room Rate: ");
            for (RoomType roomType : roomTypes) {
                System.out.println("Room Type ID: "+roomType.getId() + " Room Type Name: "+roomType.getTypeName());
            }
        }
        return roomRateId;
    }
    
    private void updateRoomRate() {
        Scanner sc = new Scanner(System.in);
        Long roomRateId;
        String roomRateName, description;
        BigDecimal rate;
        
        

        roomRateId = viewRoomRateDetails();
        
        // retrieve room rate
        RoomRate roomRate = roomControllerBeanRemote.getRoomRateById(roomRateId);
        
        System.out.println("New room rate name: ");
        roomRateName = sc.nextLine().trim();
        System.out.println("New room rate description: ");
        description = sc.nextLine().trim();
        System.out.println("New room rate per night: ");
        System.out.print("$");
        rate = sc.nextBigDecimal();
        sc.nextLine();
        
        roomControllerBeanRemote.updateRoomRate(roomRateId, roomRateName, description, rate);
    }
    
    private void createNewRoomRate() {
        Scanner sc = new Scanner(System.in);
        String roomRateName, description;
        BigDecimal rate;
        
        
        System.out.println("Please enter new room rate name:");
        roomRateName = sc.nextLine().trim();
        System.out.println("Please enter room rate description:");
        description = sc.nextLine().trim();
        System.out.println("Please enter rate per night:");
        System.out.print("$");
        rate = sc.nextBigDecimal();
        sc.nextLine();
        
        roomControllerBeanRemote.createNewRoomRate(roomRateName, description, rate);
    }
    
}
