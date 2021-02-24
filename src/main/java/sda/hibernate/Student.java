package sda.hibernate;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Student {

    @EmbeddedId //tworzy złozony klucz głowny
    private NamePK name;

    @Embedded
    private Address address;

    public Student(String firstName, String lastName, Address address) {
        this.name = new NamePK(firstName, lastName);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
     //           "id=" + id +
                ", name=" + name +
                ", address=" + address +
                '}';
    }
}
