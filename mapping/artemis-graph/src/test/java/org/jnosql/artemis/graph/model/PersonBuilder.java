/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.graph.model;

import java.util.List;

public class PersonBuilder {
    private Long id;
    private String name;
    private int age;
    private List<String> phones;
    private String ignore;

    public PersonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder withAge() {
        this.age = 10;
        return this;
    }

    public PersonBuilder withAge(int age) {
        this.age = age;
        return this;
    }


    public PersonBuilder withPhones(List<String> phones) {
        this.phones = phones;
        return this;
    }

    public PersonBuilder withIgnore() {
        this.ignore = "Just Ignore";
        return this;
    }

    public Person build() {
        return new Person(id, name, age, phones, ignore);
    }
}