package sda.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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


    @OneToMany(mappedBy = "student")
    private List<Grade> grades;

    @Embedded
    private Address address;



    public Student(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }


    private Student() {
    }

    @PrePersist
    void prePersist(){
        System.out.println("Pre persist");
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        System.out.println("Pre update");
        lastModifiedTime = LocalDateTime.now();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createTime=" + createTime +
                ", lastModifiedTime=" + lastModifiedTime +
                ", grades=" + grades +
                ", address=" + address +
                '}';
    }
}
