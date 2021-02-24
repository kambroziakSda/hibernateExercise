package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
