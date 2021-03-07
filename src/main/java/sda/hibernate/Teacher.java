package sda.hibernate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Teacher {

    @EmbeddedId
    private NamePk namePk;

    private Teacher() {
    }

    public Teacher(NamePk namePk) {
        this.namePk = namePk;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "namePk=" + namePk +
                '}';
    }
}
