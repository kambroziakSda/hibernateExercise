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

    @PrePersist
    void prePersist(){
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

    /*
    Domyślny konstruktor potrzebny dla Hibernate przy wyciaganiu obiektow z bazy,
    ale z racji tego że hibernate wykorzystuje mechanizm refleksji to widoczność tego kontruktora może być prywatna
     */
    private Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createTime=" + createTime +
                ", address=" + address +
                '}';
    }
}
