package com.min.redisson;


public record Person(String firstName, String lastName) {
    public String getFullName() {
        return firstName + lastName;
    }
}
