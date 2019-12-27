package com.example.inclass09;

public class User {
    String id;
    String firstName;
    String lastName;

    @Override
    public String toString() {
        return firstName+ " " + lastName;
    }
}
