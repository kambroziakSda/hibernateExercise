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

    @Version //powoduje automatyczne odswiezanie czasu ostatniej aktualizacji przy update
    private LocalDateTime lastModifiedTime;

    /*
        Druga możliwość to dedykowane pole pod trzymanie wersji ogolnie @Version wykorzystywane jest
        w mechanizmie optimistic lockow https://www.baeldung.com/jpa-optimistic-locking
             */
    // @Version
    //private Integer counterVersion;

    @PrePersist
    void prePersist() {
        System.out.println("Pre persist");
        createTime = LocalDateTime.now();
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
