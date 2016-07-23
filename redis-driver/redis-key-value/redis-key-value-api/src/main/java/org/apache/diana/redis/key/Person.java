package org.apache.diana.redis.key;

import java.io.Serializable;
import java.util.Objects;


public class Person implements Serializable {

    private static final long serialVersionUID = 5089852596376703955L;

    private final String name;

    private final Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }


    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}