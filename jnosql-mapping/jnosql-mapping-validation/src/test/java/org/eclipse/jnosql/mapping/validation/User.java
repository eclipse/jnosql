package org.eclipse.jnosql.mapping.validation;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    private Long id;

    @NotBlank
    @Column
    private String name;

    @Min(21)
    @NotNull
    @Column
    private Integer age;

    private User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    User() {
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Integer age() {
        return age;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static User of(String name, int age) {
        return new User(null, name, age);
    }
}
