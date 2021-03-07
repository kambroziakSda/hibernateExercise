package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory()) {


            saveStudent(sessionFactory);
            selectAndUpdate(sessionFactory);
            deletes(sessionFactory);


            //
            System.out.println("native");
            saveStudent(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Student student = session.find(Student.class, 2);

                //aktualizacja studenta
                student.setFirstName("Krzysztof");


                //student wyciagany z sesji w student2 firstName=Krzysztof
                Student student2 = session.find(Student.class, 2);
                System.out.println("student after find: " + student2);


                //dane studenta wyciagane za pomoca native query z pomienieciem sesji!, wartosc pola name='JAN'
                List students = session.createNativeQuery("select name, lastname from student").getResultList();

                System.out.println("Students from native");
                students.stream().forEach(s -> {
                    Object[] columns = (Object[]) s;
                    System.out.println(columns[0] + " " + columns[1]);
                });

                List<Student> resultList = session.createNamedQuery(Student.FIND_ALL_STUDENTS, Student.class).getResultList();

                System.out.println("Students from named query: " + resultList);

                transaction.commit();

            }

        }

    }

    private static void deletes(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student studentJan = session.find(Student.class, 1);
            System.out.println("Student from database: " + studentJan);
            session.delete(studentJan); //encja przechodzi w stan detached tzn usunieta z sesji ale jest jeszcze w bazie
            studentJan = session.find(Student.class, 1);
            System.out.println("Student after delete: " + studentJan); // null bo obiektu nie ma juz w sesji

            //Exception bo studenta nie ma juz w sesji
            try {
                studentJan = session.createQuery("SELECT s from Student s", Student.class).getSingleResult();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            transaction.commit(); //usuniecie dopiero tutaj
        }
    }

    private static void selectAndUpdate(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            //update wymaga transakcji tak samo jak insert
            Transaction transaction = session.beginTransaction();
            Student studentJan = session.find(Student.class, 1);
            System.out.println("Student from database: " + studentJan);
            studentJan.setAddress(new Address("Warszawa", "Miodowa"));
            transaction.commit(); //zapis do bazy dopiero tutaj
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
