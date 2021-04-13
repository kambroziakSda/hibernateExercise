package sda.hibernate;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    Address() {
    }

    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }

    private String city;

    private String street;

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
