package sda.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name") //adnotacja @Column zmienia domyślną definicje pola
    private String firstName;
    private String lastName;

    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;

    @PostLoad
    public void postLoad(){
        System.out.println("[Student] Post load");
    }


    @PrePersist
    void prePersist(){
        System.out.println("[Student] Pre persist");
        createTime = LocalDateTime.now();
    }

    @PostPersist
    void postPersist(){
        System.out.println("[Student] Post persist");
    }

    @PreUpdate
    void preUpdate(){
        System.out.println("[Student] Pre update");
        lastModifiedTime = LocalDateTime.now();
    }

    @PostUpdate
    void postUpdate(){
        System.out.println("[Student] Post update");
    }

    @PreRemove
    void preRemove(){
        System.out.println("[Student] Pre remove");
    }

    @PostRemove
    void postRemove(){
        System.out.println("[Student] Post remove");
    }

    @Embedded
    private Address address;

    public Student(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }


    private Student() {
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createTime=" + createTime +
                ", lastModifiedTime=" + lastModifiedTime +
                ", address=" + address +
                '}';
    }
}
