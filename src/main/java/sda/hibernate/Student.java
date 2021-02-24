package sda.hibernate;

import javax.persistence.*;

//@Entity(name = "StudentEntity") Adnotacja entity pozwala na zmiane domyślnej nazwy ecnji (uwaga: nie mylić z nazwą tabeli w bazie danych)
//@Table(name = "tabstudent")
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@SequenceGenerator()
    private Integer id;

    @Column(name = "name") //adnotacja @Column zmienia domyślną definicje pola
    private String firstName;
    private String lastName;

    @Embedded
    private Address address;

    public Student(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    /*
            Getery i settery nie są konieczne, hibernate uzywa pol klasy i mechanizmu refleksji w celu ustawienia
            wartości dla poszczegolnych pól, chyba że adnotacje modelujące encje bedziemy umieszczać nad geterami,
            wtedy zarowno gettery jak i settery do pól są konieczne, nie można pomieszać tych podejść czyli umieścić
            cześć adnotacji nad polami a część nad geterami
               */
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
