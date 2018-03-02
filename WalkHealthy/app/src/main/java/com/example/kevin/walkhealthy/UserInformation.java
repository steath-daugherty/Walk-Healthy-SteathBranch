package com.example.kevin.walkhealthy;

/**
 * Created by Kevin on 9/29/2017.
 */

public class UserInformation {

    public String username;
    public String city;
    public String state;
    public String college;

    public UserInformation(){

    }

    public UserInformation(String username, String city, String state, String college) {
        this.username = username;
        this.city = city;
        this.state = state;
        this.college = college;
    }
}
