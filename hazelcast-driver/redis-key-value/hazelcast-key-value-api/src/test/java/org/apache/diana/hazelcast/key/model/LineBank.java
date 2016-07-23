package org.apache.diana.hazelcast.key.model;


import java.io.Serializable;
import java.util.Objects;

public class LineBank implements Serializable {


    private final Person person;

    public Person getPerson() {
        return person;
    }

    public LineBank(String name, Integer age) {
        this.person = new Person(name, age);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineBank lineBank = (LineBank) o;
        return Objects.equals(person, lineBank.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LineBank{");
        sb.append("person=").append(person);
        sb.append('}');
        return sb.toString();
    }
}