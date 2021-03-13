package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;

/*
Zapis do bazy w transakcji
Lifecycle methods: prePersist, postPersist, StudentEntityEventListener
Graf przejsc stanów encji
API Hibernate vs API JPA
 */
public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory()) {


            try (Session session = sessionFactory.openSession()) {

            //jpa api
                System.out.println("[main] JPA persist");
                Transaction transaction = session.beginTransaction();
                Student studentJan = new Student("Jan", "Kowalski", new Address("Gdańsk", "Grunwaldzka"));

                /*
                tutaj nie ma jescze zapisu, hibernate moze nawet sqli nie wysłac do bazy,
                To kiedy sqle wysyłane sa do bazy sterowane jest przez FlushMode
                https://docs.jboss.org/hibernate/orm/5.2/javadocs/org/hibernate/FlushMode.html
                 */
                session.persist(studentJan);

                System.out.println("[main] Before commit");
                transaction.commit(); //zapis do bazy dopiero tutaj


            //hibernate api
                System.out.println("[main] Hibernate save");
                transaction = session.beginTransaction();
                Student studentAla = new Student("Ala", "Kowalska", new Address("Gdańsk", "Grunwaldzka"));
                Serializable id = session.save(studentAla);
                System.out.println("[main] save returned: " + id);

                transaction.commit();
            }
        }

    }

}
