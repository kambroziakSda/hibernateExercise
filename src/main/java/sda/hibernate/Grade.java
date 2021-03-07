package sda.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "type")
public abstract class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @ManyToOne //domyślnie EAGER!
    @JoinColumn(name = "idteacher")
    private Teacher teacher;

    @ManyToOne //domyślnie EAGER!
    @JoinColumn(name = "idstudent")
    private Student student;

    private LocalDateTime createTime;

    Grade() {
    }

    protected Grade(Teacher teacher, Student student, LocalDateTime createTime) {
        this.teacher = teacher;
        this.student = student;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", student=" + student.getId() +
                ", createTime=" + createTime +
                '}';
    }
}
