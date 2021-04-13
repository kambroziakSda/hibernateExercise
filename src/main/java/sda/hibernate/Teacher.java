package sda.hibernate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Teacher {

    Teacher() {
    }

    public Teacher(NamePk name) {
        this.name = name;
    }

    @EmbeddedId
    private NamePk name;

}
