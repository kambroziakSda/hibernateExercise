package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
            saveStudent(sessionFactory, "Alice", "Nowak");
            selectAndUpdate(sessionFactory);
            //  delete(sessionFactory);
            Teacher teacher = new Teacher("Krzysztof");
            addGradesForStudents(sessionFactory, teacher);
            joinFetch(sessionFactory);
            cascadeDelete(sessionFactory);
            orphanRemvoval(sessionFactory);
            cascadeAdd(sessionFactory, teacher);


            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Student student2 = session.find(Student.class, 2);
                Student student3 = session.find(Student.class, 3);

            Set<Student> studentSet = new HashSet<>();
                studentSet.add(student2);
                studentSet.add(student3);
                Academy superCoder = new Academy("SuperCoder",  studentSet);
                Academy academy = new Academy("SDA", Collections.singleton(student2));

                session.persist(superCoder);
                session.persist(academy);

                transaction.commit();

            }



        }

    }

    private static void orphanRemvoval(SessionFactory sessionFactory) {
        //usuwanie elementow z relacji a usuwanie z bazy poniższe nie spowoduje usunięcia ocen studenta z bazy
        // chyba że nad polem student.grades ustawimy opcje orphanRemoval=true
        System.out.println("Usuniecie ocen drugiego studenta: ");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.find(Student.class, 2);
            student.getGrades().clear(); // usuwa oceny tylko jesli orphanRemoval = true na polu students.grades
            transaction.commit();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void cascadeDelete(SessionFactory sessionFactory) {
    /*
                usuwanie studenta w raz  z ocenami, ponizsze nie zadziała bo mamy klucz obcy w tabeli grade.idstudent,
                wiec albo trzeba najpierw usunac wszystkie oceny studenta a potem samego studenta albo użyć Cascade
                na polu student.grades;
      */
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.find(Student.class, 1);
            session.delete(student);
            transaction.commit();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void cascadeAdd(SessionFactory sessionFactory, Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student2 = session.find(Student.class, 2);
            student2.getGrades().add(new Grade(2, teacher, student2, LocalDateTime.now()));
            session.persist(student2);

            transaction.commit();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void joinFetch(SessionFactory sessionFactory) {
        System.out.println("Before selecting students ");
        try (Session session = sessionFactory.openSession()) {
            // join fetch powoduje wyciagniecie encji powiązanych za pomocą SQL JOIN
            List<Student> students = session.createQuery("SELECT s FROM Student s JOIN FETCH s.grades g JOIN FETCH g.teacher", Student.class).getResultList();
            System.out.println("Student with grades: " + students);
        }
    }

    private static void addGradesForStudents(SessionFactory sessionFactory, Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.find(Student.class, 1);
            Student student2 = session.find(Student.class, 2);

            session.persist(teacher);
            addGrades(session, student, student2, teacher);
            transaction.commit();
        }
    }

    private static void addGrades(Session session, Student student, Student student2, Teacher teacher) {
        Grade grade = new Grade(5, teacher, student, LocalDateTime.now());
        Grade grade2 = new Grade(4, teacher, student, LocalDateTime.now());
        Grade grade3 = new Grade(2, teacher, student2, LocalDateTime.now());
        Grade grade4 = new Grade(3, teacher, student2, LocalDateTime.now());
        session.persist(grade);
        session.persist(grade2);
        session.persist(grade3);
        session.persist(grade4);
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
