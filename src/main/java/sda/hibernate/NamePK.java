package sda.hibernate;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class NamePK implements Serializable {

    private String firstName;

    private String lastName;

    NamePK() {
    }

    public NamePK(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "NamePK{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
