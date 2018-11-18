/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import entity.Reservation;
import entity.Room;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import session.stateless.AccountControllerBeanRemote;
import session.stateless.RoomControllerBeanRemote;

/**
 *
 * @author caidi
 */
public class MainApp {
    private RoomControllerBeanRemote roomControllerBeanRemote;
    private AccountControllerBeanRemote accountControllerBeanRemote;
    private String loginAcc = "";

    public MainApp() {
    }

    public MainApp(RoomControllerBeanRemote roomControllerBeanRemote, AccountControllerBeanRemote accountControllerBeanRemote) {
        this.roomControllerBeanRemote = roomControllerBeanRemote;
        this.accountControllerBeanRemote = accountControllerBeanRemote;
    }
    
    public void landingPate() {
        Scanner sc = new Scanner(System.in);
        Integer response = -1;
        
        while (true) {            
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("************************");
            System.out.println("Welcome to HoRS Reservation Client!");
            System.out.println("Main Menu:");
            System.out.println("0. Exit Progrom");
            System.out.println("************************");
            System.out.println("1. Log in as partner");
            System.out.println("2. Log in as guest");
            System.out.println("3. Create a guest account");
            
            
            // clear response from last iteration
            response = -1;
            
            while (response < 0 || response > 5) {                
                System.out.print(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    loginAsPartner();
                } 
                else if (response == 2) {
                    loginAsGuest();
                }
                else if (response == 3) {
                    createNewGuest();
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
    
    private void partnerPage() {
        Scanner sc = new Scanner(System.in);
        Integer response = -1;
        
        while (true) {            
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("************************");
            System.out.println("HoRS Partner Page!");
            System.out.println("Welcome " + loginAcc);
            System.out.println("0. Logout ");
            System.out.println("************************");
            System.out.println("1. View All Available Rooms"); 
            System.out.println("2. Create New Reservation");
            System.out.println("3. Process Current Reservations");
            System.out.println("4. Display Current Resservations");
            
            // clear response from last iteration
            response = -1;
            
            while (response < 0 || response > 4) {                
                System.out.print(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    viewAllAvailableRooms();
                } 
                else if (response == 2) {
                    createNewReservation();
                }
                else if (response == 3) {
                    processReservation();
                }
                else if (response == 4) {
                    viewAllReservations();
                }
                else if (response == 0) {
                    loginAcc = "";
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            // logout
            if (response == 0) {
                loginAcc = "";
                break;
            }
        }
    }
    
    private void guestPage() {
        Scanner sc = new Scanner(System.in);
        Integer response = -1;
        
        while (true) {            
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("************************");
            System.out.println("HoRS Partner Page!");
            System.out.println("Welcome " + loginAcc);
            System.out.println("0. Logout ");
            System.out.println("************************");
            System.out.println("1. View All Available Rooms"); 
            System.out.println("2. Create New Reservation");
            System.out.println("3. View My Reservations");
            
            // clear response from last iteration
            response = -1;
            
            while (response < 0 || response > 3) {                
                System.out.print(">");
                response = sc.nextInt();
                
                if (response == 1) {
                    viewAllAvailableRooms();
                } 
                else if (response == 2) {
                    createNewReservation();
                }
                else if (response == 3) {
                    viewAllReservationsByGuestName();
                }

                else if (response == 0) {
                    loginAcc = "";
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            // logout
            if (response == 0) {
                loginAcc = "";
                break;
            }
        }
    }
    
    
    
    // partner page methods
    private void loginAsPartner() {
        Scanner sc = new Scanner(System.in);
        Boolean authorized = false;
        String accountName, password;
        
        
        System.out.println("Please enter account name:");
        accountName = sc.nextLine().trim();
        System.out.println("Please enter password");
        password = sc.nextLine().trim();
        
        try {
            authorized = accountControllerBeanRemote.partnerAuthentication(accountName, password);
                if (authorized) {
                loginAcc = accountName;
                System.out.println(accountName + " login successfully");
                partnerPage();
            }
            else {
                System.out.println("Incorrect account name or password");
            }
        } catch (Exception e) {
            System.out.println("Employee account not found");
        }
    }
    
    private void viewAllAvailableRooms() {
        List<Room> rooms = roomControllerBeanRemote.retrieveAllAvailiableRooms();
        if (rooms.size() == 0) {
            System.out.println("No available rooms now!");
        }
        else {
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
    }
    
    private void createNewReservation() {
        Scanner sc = new Scanner(System.in);
        String checkInDateString, checkOutDateString;
        Date checkInDate, checkOutDate;
        Long roomId;
        List<Room> availableRooms;
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // initilise date
        checkInDate = new Date(0, 0, 0);
        checkOutDate = new Date(0, 0, 0);
        
        availableRooms = roomControllerBeanRemote.retrieveAllAvailiableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available room now");
            return;
        } 
        else {
            // display all available rooms
            for(Room room:availableRooms) {
                String result;
                result = "Room id: "+room.getId()+" Room Number: "+ room.getRoomNumber() + " Room available: " + room.getStatus() + " Room is disabled: " + room.getIsDisabled();
                try {
                    result = result + " Room Type: " + room.getRoomType().getTypeName();
                } catch (Exception e) {
                    result = result + " Room Type: No Room Type Assigned Yet";
                }
                System.out.println(result);
            }
            
            // promt user enter reservation details
            
            System.out.println("Please enter check in date (dd/MM/yyy)");
            checkInDateString = sc.nextLine().trim();
            try {
                checkInDate = sourceFormat.parse(checkInDateString);
            } catch (Exception e) {
                System.out.println("Date format not correct");
            }
            
            System.out.println("Please enter check out date (dd/MM/yyy)");
            checkOutDateString = sc.nextLine().trim();
            
            try {
                checkInDate = sourceFormat.parse(checkOutDateString);
            } catch (Exception e) {
                System.out.println("Date format not correct");
                return;
            }
            
            System.out.println("Please enter room ID: ");
            roomId = sc.nextLong();
            sc.nextLine();
            
            roomControllerBeanRemote.createNewReservation(loginAcc, checkInDate, checkOutDate, roomId);
            System.out.println("Reservation created successfully!");
        }
    }
    
    private void viewAllReservations() {
        List<Reservation> reservations = roomControllerBeanRemote.retrieveAllReservations();
        if (reservations.size() == 0) {
            System.out.println("No reservation found!");
        } 
        else {
            for (Reservation reservation : reservations) {
                Room room = roomControllerBeanRemote.getRoomById(reservation.getRoomId());
                System.out.println("Reservation ID: "+reservation.getId()+" Guest Name: "+reservation.getGuestName()+" Room Number: "+room.getRoomNumber()+" Check In Date: "+reservation.getCheckInDate().toString() + " Check Out Date: "+reservation.getCheckOutDate().toString());
                
                
            }
        }
    }
    
    private void processReservation() {
        System.out.println(roomControllerBeanRemote.processReservations());
    }
    
    private void createNewGuest() {
        Scanner sc = new Scanner(System.in);
        Boolean authorized = false;
        String email, password;
        
        
        System.out.println("Please enter account name:");
        email = sc.nextLine().trim();
        System.out.println("Please enter password");
        password = sc.nextLine().trim();
        
        try {
            accountControllerBeanRemote.createNewGuest(email, password);
        } catch (Exception e) {
            System.out.println("Account name existes, please try with a different account name!");
            return;
        }
        
        System.out.println("Guest created successfully");
    }
    
    private void loginAsGuest() {
        Scanner sc = new Scanner(System.in);
        Boolean authorized = false;
        String accountName, password;
        
        
        System.out.println("Please enter account name:");
        accountName = sc.nextLine().trim();
        System.out.println("Please enter password");
        password = sc.nextLine().trim();
        
        try {
            authorized = accountControllerBeanRemote.guestAuthentication(accountName, password);
                if (authorized) {
                loginAcc = accountName;
                System.out.println(accountName + " login successfully");
                guestPage();
            }
            else {
                System.out.println("Incorrect account name or password");
            }
        } catch (Exception e) {
            System.out.println("Guest account not found");
        }
    }
    
    private void viewAllReservationsByGuestName() {
        List<Reservation> reservations = roomControllerBeanRemote.retrieveAllReservations();
        if (reservations.size() == 0) {
            System.out.println("No reservation found!");
        } 
        else {
            int guestReservationCount = 0;
            for (Reservation reservation : reservations) {
                if (reservation.getGuestName().equals(loginAcc)) {
                    guestReservationCount++;
                    Room room = roomControllerBeanRemote.getRoomById(reservation.getRoomId());
                    System.out.println("Reservation ID: "+reservation.getId()+" Guest Name: "+reservation.getGuestName()+" Room Number: "+room.getRoomNumber()+" Check In Date: "+reservation.getCheckInDate().toString() + " Check Out Date: "+reservation.getCheckOutDate().toString());
                }
            }
            if (guestReservationCount == 0) {
                System.out.println("No reservation found!");
            }
        }
    }
}
