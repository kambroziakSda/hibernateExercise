package sda.hibernate;

import javax.persistence.*;

public class Student {

    private String firstName;
    private String lastName;



    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
