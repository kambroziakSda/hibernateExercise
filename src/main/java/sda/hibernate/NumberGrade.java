package sda.hibernate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@DiscriminatorValue(value = "num")
@Entity
public class NumberGrade extends Grade {

    private int value;

    protected NumberGrade() {
    }

    public NumberGrade(int value, Teacher teacher, Student student, LocalDateTime createTime) {
        super(teacher, student, createTime);
        this.value = value;
    }

    @Override
    public String toString() {
        return super.toString() + "NumberGrade{" +
                "value=" + value +
                '}';
    }
}
