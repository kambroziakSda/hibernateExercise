package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*
1. Modelowanie: @id,@GeneratedValue, @Column, bezparametrowy konstruktor, @entity, getery i settery nie sa niezbedne, @Embedded
2. generowanie skryptu bazy na podstawie encji
3.
 */

public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory()) {

            Student student = new Student("Jan", "Kowalski", new Address("Gdańsk","Grunwaldzka"));

            try (Session session = sessionFactory.openSession()) {

            }


        }

    }

}
