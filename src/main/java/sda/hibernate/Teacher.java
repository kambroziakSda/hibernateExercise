package sda.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Teacher {

    @Id
    private String name;

    private Teacher() {
    }

    public Teacher(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                '}';
    }
}
