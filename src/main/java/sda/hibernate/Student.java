package sda.hibernate;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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


    /*
             @OneToMany(mappedBy = "student", fetch = FetchType.EAGER) eager powoduje dociagniecie relacji zawsze za pomoca jedej z trzech metod: subselect, join lub n-select
             //lazy jest domyslne ale tylko dla OneToMany, powoduje dociaganie encji z relacji w miare potrzeby
             warto zwrocic uwage na n+1 select problem https://vladmihalcea.com/n-plus-1-query-problem/

            Opcja cascade mówi czy operacje wykonywane na głownej encji w tym wypadku Student powinny być propagowane na enje powiązane
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades;

    @Embedded
    private Address address;

    @ManyToMany(mappedBy = "students")
    private Set<Academy> academies;


    public Student(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }


    private Student() {
    }

    @PrePersist
    void prePersist() {
        System.out.println("Pre persist");
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        System.out.println("Pre update");
        lastModifiedTime = LocalDateTime.now();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Set<Academy> getAcademies() {
        return academies;
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
