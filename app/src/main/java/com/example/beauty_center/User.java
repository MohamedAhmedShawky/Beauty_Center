package com.example.beauty_center;


import java.util.List;
import java.util.ArrayList;

public class User {
    private String FirstName, LastName, Beauty_Center_name, Address, OpenAt, CloseAt, FirstService, SecondService, Email, Phone, ID, Type, Offer;
    private double longitude;
    private double latitude;


    public int NumberOfReservation;
    public int Completed;
    public int Current;
    public int TicketNumber;
    public int Waiting;

   // List<service> serviceList=new ArrayList<>();



    //for test
   // List<String> services2 = new ArrayList<>();

   /* public void setServiceList(List<service> serviceList) {
        this.serviceList = serviceList;
    }
*/


    public User() {
    }

    public User(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;
    }

    public User(String firstName, String lastName, String beauty_Center_name, String address, String openAt, String closeAt,
                String firstService, String secondService, String phone) {
        FirstName = firstName;
        LastName = lastName;
        Beauty_Center_name = beauty_Center_name;
        Address = address;
        OpenAt = openAt;
        CloseAt = closeAt;
        FirstService = firstService;
        //add in the test
       // this.services2=services2;
        SecondService = secondService;
        Phone = phone;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    /*public String getFirstName() {
        return FirstName;
    }

    public void setName(String firstName) {
        FirstName = firstName;
    }
*/


    public String getBeauty_Center_name() {
        return Beauty_Center_name;
    }

    public void setBeauty_Center_name(String beauty_Center_name) {
        Beauty_Center_name = beauty_Center_name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOpenAt() {
        return OpenAt;
    }

    public void setOpenAt(String openAt) {
        OpenAt = openAt;
    }

    public String getCloseAt() {
        return CloseAt;
    }

    public void setCloseAt(String closeAt) {
        CloseAt = closeAt;
    }

    public String getFirstService() {
        return FirstService;
    }

    public void setFirstService(String firstService) {
        FirstService = firstService;
    }

    public String getSecondService() {
        return SecondService;
    }

    public void setSecondService(String secondService) {
        SecondService = secondService;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



}
