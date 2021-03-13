package sda.hibernate;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

public class StudentEntityEventListener {

    @PrePersist
    void prePersist(Student student){
        System.out.println("[StudentListener] Pre persist");
    }

    @PostPersist
    void postPersist(Student student){
        System.out.println("[StudentListener] Post persist");
    }

}
