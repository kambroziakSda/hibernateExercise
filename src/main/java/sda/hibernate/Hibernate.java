package sda.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/*
1.Głowne klasy:
    1.1 SessionFactory
    1.2 Session
2.Konfiguracja - hibernate.cfg.xml /persistance.xml
3.Logowanie zapytań
4.Generowanie schematu bazy i wypełnanie danymi
 */
public class Hibernate {

    public static void main(String[] args) throws InterruptedException {
        try (final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Teacher.class)
                .addAnnotatedClass(Director.class)
                .buildSessionFactory()) {


            inserts(sessionFactory);

            selects(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                System.out.println("Before update");
                Transaction transaction = session.beginTransaction();
                Student student = session.find(Student.class, 1);
                student.setAddress(new Address("Poznań", "Głogowska"));
                //session.update(student); update automatyczny wyołanie metody niepotrzebne

                transaction.commit();
            }

            Student studentAfterUpdate;
            try (Session session = sessionFactory.openSession()) {
                studentAfterUpdate = session.find(Student.class, 1);
                System.out.println("Student after update: " + studentAfterUpdate);
            }

            System.out.println("Before update in new session");
            try(Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                studentAfterUpdate.setAddress(new Address("Kraków","Mickiewicza"));
                session.update(studentAfterUpdate); // lub session.merge() jesli uzywamy JPA
                transaction.commit();
            }

        }


    }

    private static void selects(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            System.out.println("Persist student and select");
            int studentId = 1;
            Transaction transaction = session.beginTransaction();
            Student studentToPersist = new Student("Adam", "Adamowski", new Address("Sopot", "Monciak"));
            session.persist(studentToPersist); // wstawienie do wewnatrz sesji
            Integer persistedStudentId = studentToPersist.getId();
            System.out.println("Just persisted student id: " + persistedStudentId);
            Optional<Student> student = Optional.ofNullable(session.find(Student.class, persistedStudentId));

            System.out.println("Student with id: " + studentId + ": " + student.toString());

            transaction.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Student student = session.find(Student.class, 3);
            System.out.println("Student 3: " + student);
        }

        try (Session session = sessionFactory.openSession()) {
            //uwaga na wyjątki:
            //https://javaee.github.io/javaee-spec/javadocs/javax/persistence/TypedQuery.html?is-external=true#getSingleResult--
            int studentId = 1;
            try {
                Query<Student> query = session.createQuery("SELECT s FROM Student s WHERE s.id = :studentId ", Student.class);
                Student student1 = query
                        .setParameter("studentId", studentId)
                        .getSingleResult();

                System.out.println("Student from get single result: " + student1);
            } catch (NoResultException e) {
                System.out.println("No results for id: " + studentId);
            }
            /*
            catch (NonUniqueResultException e){
            }*/ // w tym wypadku nie ma sensu bo odpytujemy po kluczu głównym wiec zawsze bedzie max jeden resultat

            String studentName = "Jan";
            Query<Student> query = session.createQuery("SELECT s FROM Student s WHERE s.firstName = :firstName", Student.class);
            List<Student> students = query
                    .setParameter("firstName", studentName)
                    .setMaxResults(10)
                    .setFirstResult(0) // przydatne do mechanizmów stronicowania
                    .getResultList();

            System.out.println("Students from get result list " + students);


            String studentName2 = "Jan";
            Query<Student> query2 = session.createQuery("SELECT s FROM Student s WHERE s.firstName = :firstName", Student.class);
            Student students2 = query2
                    .setParameter("firstName", studentName2)
                    .setMaxResults(1)
                    .getSingleResult();

            System.out.println("Student 2: " + students2);

        }

        System.out.println("Before native query");
        try (Session session = sessionFactory.openSession()) {
            Object studentId = session.createNativeQuery("select name from tab_student where id = :studentId") //posługujemy sie nazwami kolumn z bazy a nie polami z klasy
                    .setParameter("studentId", 1)
                    .getSingleResult();

            System.out.println("Native query: " + studentId);
        }

        try (Session session = sessionFactory.openSession()) {
            Query<StudentDTO> query2 = session.createQuery("SELECT new sda.hibernate.StudentDTO( s.firstName, s.lastName) " +
                    "FROM Student s WHERE s.firstName = :firstName", StudentDTO.class)
                    .setParameter("firstName", "Jan");

            List<StudentDTO> studentDTOS = query2.getResultList();

            System.out.println("Students dtos: " + studentDTOS);

        }

        try (Session session = sessionFactory.openSession()) {
            Query<Student> namedQuery = session.createNamedQuery(Student.FIND_STUDENT_BY_FIRST_NAME, Student.class);
            List<Student> students = namedQuery
                    .setParameter("firstName", "Jan")
                    .getResultList();

            System.out.println("Students from named query: " + students);
        }
    }

    private static void inserts(SessionFactory sessionFactory) {
        //insert

        Address address = new Address("Gdańsk", "Grunwaldzka");
        Student student = new Student("Jan", "Kowalski", address);
        System.out.println("Insert student 1");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(student); // metoda z standardu JPA


            transaction.commit(); // tutaj zapis do bazy danych

            System.out.println("Student added with id: " + student.getId());
        }

        Address address2 = new Address("Warszwa", "Wiejska");
        Student student2 = new Student("Andrzej", "Nowak", address2);
        System.out.println("Insert student 2");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Serializable serializable = session.save(student2); //metoda z API Hibernate
            transaction.commit();
            System.out.println("Student added with id: " + serializable);
            System.out.println("Student id class: " + serializable.getClass().getName());
        }
        try (Session session = sessionFactory.openSession()) {
            System.out.println("Insert teacher 1");
            Teacher teacher = new Teacher(new NamePk("Janina", "Kowalska"));
            Teacher teacher2 = new Teacher(new NamePk("Janina", "Nowak"));
            Transaction transaction = session.beginTransaction();
            Serializable serializable = session.save(teacher); //metoda z API Hibernate
            System.out.println("Insert teacher 2");
            session.save(teacher2);
            System.out.println("Commit teacher 1, teacher 2");
            transaction.commit();
            System.out.println("Teacher added with id: " + serializable);
            System.out.println("Teacher id class: " + serializable.getClass().getName());
        }
    }

}
