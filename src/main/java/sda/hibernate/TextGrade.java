package sda.hibernate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@DiscriminatorValue("text")
@Entity
public class TextGrade extends Grade {

    private String textGrade;

    private TextGrade() {
    }

    protected TextGrade(Teacher teacher, Student student, LocalDateTime createTime, String textGrade) {
        super(teacher, student, createTime);
        this.textGrade = textGrade;
    }

    @Override
    public String toString() {
        return super.toString() + "TextGrade{" +
                "textGrade='" + textGrade + '\'' +
                '}';
    }
}
