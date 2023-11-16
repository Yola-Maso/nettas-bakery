package com.example.nettas_bakery;

public class UserData {

    private static UserData instance;
    private String phoneNumber;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private UserData() {
        // Private constructor to prevent instantiation
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getPhoneNumber() {

        System.out.println("Phone Number: " + phoneNumber);
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
