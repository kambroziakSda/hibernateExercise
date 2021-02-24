package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;

public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory()) {


            saveStudent(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                //update wymaga transakcji tak samo jak insert
                Transaction transaction = session.beginTransaction();
                Student studentJan = session.find(Student.class, 1);
                System.out.println("Student from database: " + studentJan);
                studentJan.setAddress(new Address("Warszawa","Miodowa"));
                transaction.commit();

            }
        }

    }

    private static void saveStudent(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            //jpa api
            Transaction transaction = session.beginTransaction();
            Student studentJan = new Student("Jan", "Kowalski", new Address("Gdańsk", "Grunwaldzka"));
            session.persist(studentJan);
            System.out.println("Before commit");
            transaction.commit(); //zapis do bazy dopiero tutaj
        }
    }

}
