package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
/*
-Sesja jako cache; find nie zawsze wyciaga z bazy
-Update wymaga transakcji
-Nie jest konieczne wołanie metody update
-W obrebie jednej sesji hibernate zawsze zwraca ten sam obiekt - przykład z equals
-Lifecycle method preUpdate, postUpdate
 */
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
                System.out.println("[main] Student from database: " + studentJan);
                studentJan.setAddress(new Address("Warszawa","Miodowa"));
                transaction.commit(); //zapis do bazy dopiero tutaj
            }

            /*
            Poniższy przykład pokazuje działanie obiektu sesji jako cache dla encji. Aktualizacja danych jest widoczna
            po drugim wywołaniu find mimo że nie było zapisu do bazy. Obiekt jest zmieniany na poziomie sesji i nastepnie z niej wyciagany.
            Porownanie .equals pokazuje ze find 1 i find 2 zwraca ten sam obiekt
             */

            try (Session session = sessionFactory.openSession()) {
                //update wymaga transakcji tak samo jak insert
                Transaction transaction = session.beginTransaction();
                Student studentJan = session.find(Student.class, 1); //find 1
                System.out.println("[main] Student from database after first update: " + studentJan);
                studentJan.setAddress(new Address("Warszawa","Wiejska"));

                Student studentJan2 = session.find(Student.class, 1); //find 2

                System.out.println("[main] Student from database after second update: " + studentJan2);
                System.out.println("[main] Czy to ten sam obiekt studenta: " + studentJan.equals(studentJan2));

                transaction.commit(); //zapis do bazy dopiero tutaj
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
