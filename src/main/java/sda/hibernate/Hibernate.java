package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/*
metoda find i zapytania HQL z parametrami
Ustawianie maksymalnej ilosci rezultatow oraz pierwszego rezultatu
select new
getSingleResult - exception w wypadku nieunikalnych lub braku rezultatu
 */

public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory()) {


            saveStudent(sessionFactory, "Jan", "Kowalski");
            saveStudent(sessionFactory, "Adam", "Nowak");

            try (Session session = sessionFactory.openSession()) {
                selectByFind(session);
                selectListByHql(session);
                notUniqueResultsException(session);
            }
        }

    }

    private static void notUniqueResultsException(Session session) {
        try {
            session.createQuery("SELECT s from Student s where s.address.city = :city ", Student.class)
                    .setParameter("city", "Gdańsk")
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());

        }

        Student student = session.createQuery("SELECT s from Student s where s.address.city = :city ", Student.class)
                .setParameter("city", "Gdańsk")
                .setMaxResults(1)
                .getSingleResult();

        System.out.println("One student from list: " + student);
    }

    private static void selectListByHql(Session session) {
        //hql
        //uwaga 1: s.address.street a nie s.street czyli kierujemy sie struktura w klasie a nie w bazie danych
        //uwaga 2:
        /*
        .setMaxResults(10)
        .setFirstResult(1)
        Stosowane do mechanizmow stronicowania;
        Ważne żeby zawsze starać się ustawiać maxResults po to aby przypadkiem nie spowodować wyciągniecia dużej ilości rekordów z bazy co mogłoby skutkowac nawet OOM
         */
        List<Student> studentsFromGrunwaldzka = session.createQuery("SELECT s from Student s where s.address.street = :street ", Student.class)
                .setParameter("street", "Grunwaldzka")
                .setMaxResults(10)
                // .setFirstResult(1)
                .getResultList();

        System.out.println("Students from grunwaldzka: " + studentsFromGrunwaldzka);


        //wyciaganie konkretnych pol zamiast całych encji:
        List resultList = session.createQuery("select s.firstName, s.lastName from Student s")
                .getResultList();

        System.out.println("First name: "+((Object[])resultList.get(0))[0]);
        System.out.println("Last name: "+((Object[])resultList.get(0))[1]);

        // lepsze rozwiazanie, select new, zwrocmy uwage ze uzywana jest nowa klasa StudentDTO
        List<StudentDTO> studentDTOS = session.createQuery("select new sda.hibernate.StudentDTO(s.firstName, s.lastName) from Student s", StudentDTO.class)
                .getResultList();

        System.out.println("StudentDTOS: "+studentDTOS);

    }

    private static void selectByFind(Session session) {
        //zwróćmy uwagę że sama operacja find nie wymaga transakcji
        Student studentJan = session.find(Student.class, 1);
        System.out.println("Student from database: " + studentJan);
    }

    private static void saveStudent(SessionFactory sessionFactory, String firstName, String lastname) {
        try (Session session = sessionFactory.openSession()) {
            //jpa api
            Transaction transaction = session.beginTransaction();
            Student studentJan = new Student(firstName, lastname, new Address("Gdańsk", "Grunwaldzka"));
            session.persist(studentJan);
            System.out.println("Before commit");
            transaction.commit(); //zapis do bazy dopiero tutaj
        }
    }

}
