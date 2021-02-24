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


            //student pobrany w osobnej sesji
            Student studentJan = saveStudent(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Thread.sleep(5000); // w celu zademonstrowania mechanizmu version i optymisticlockingu
                System.out.println("Student from database: " + studentJan);

                //wciagniecie studenta do aktualnej sesji
                Student mergedStudent = (Student) session.merge(studentJan);
                mergedStudent.setAddress(new Address("Warszawa","Miodowa"));
                transaction.commit();

            }
        }

    }

    private static Student saveStudent(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            //jpa api
            Transaction transaction = session.beginTransaction();
            Student studentJan = new Student("Jan", "Kowalski", new Address("Gdańsk", "Grunwaldzka"));
            session.persist(studentJan);
            System.out.println("Before commit");
            transaction.commit(); //zapis do bazy dopiero tutaj
            return studentJan;
        }
    }

}
