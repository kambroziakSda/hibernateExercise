package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
- Modelowanie relacji na podstawie encji grade, teacher, student, academy
- n-n modelowane jako Set
 */
public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Grade.class)
                .addAnnotatedClass(Teacher.class)
                .addAnnotatedClass(Academy.class)
                .buildSessionFactory()) {


            saveStudent(sessionFactory, "Jan", "Kowalski");
            saveStudent(sessionFactory, "Adam", "Nowak");
            selectAndUpdate(sessionFactory);
            //  delete(sessionFactory);
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Student student = session.find(Student.class, 1);
                Teacher teacher = new Teacher("Krzysztof");
                session.persist(teacher);
                Grade grade = new Grade(5, teacher, student, LocalDateTime.now());
                Grade grade2 = new Grade(4, teacher, student, LocalDateTime.now());
                session.persist(grade);
                session.persist(grade2);
                transaction.commit();
            }

            System.out.println("Before selecting student with grades: ");
            try (Session session = sessionFactory.openSession()) {
                Student student = session.find(Student.class, 1);
                System.out.println("Student with grades: " + student);
            }

            //przykład relacji wiele do wiele
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Student student = session.find(Student.class, 1);
                Student student2 = session.find(Student.class, 2);
                Academy academy = new Academy("SDA", Collections.singleton(student));
                Set<Student> studentSet = new HashSet<>();
                studentSet.add(student2);
                studentSet.add(student);
                Academy superCoder = new Academy("SuperCoder", studentSet);

                session.persist(academy);
                session.persist(superCoder);
                transaction.commit();

            }

        }

    }

    private static void delete(SessionFactory sessionFactory) {
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

    private static void saveStudent(SessionFactory sessionFactory, String firstName, String lastName) {
        try (Session session = sessionFactory.openSession()) {
            //jpa api
            Transaction transaction = session.beginTransaction();
            Student studentJan = new Student(firstName, lastName, new Address("Gdańsk", "Grunwaldzka"));
            session.persist(studentJan);
            System.out.println("Before commit");
            transaction.commit(); //zapis do bazy dopiero tutaj
        }
    }

}
