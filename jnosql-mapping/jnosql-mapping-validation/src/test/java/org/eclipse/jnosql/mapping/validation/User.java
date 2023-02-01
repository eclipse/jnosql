/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
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
