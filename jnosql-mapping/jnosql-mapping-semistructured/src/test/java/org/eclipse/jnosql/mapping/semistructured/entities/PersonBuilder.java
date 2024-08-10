/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package org.eclipse.jnosql.mapping.semistructured.entities;

import java.util.List;

public class PersonBuilder {
    private long id;
    private String name;
    private int age;
    private List<String> phones;
    private String ignore;
    private String[] mobiles;

    public PersonBuilder id(long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder age() {
        this.age = 10;
        return this;
    }

    public PersonBuilder age(int age) {
        this.age = age;
        return this;
    }

    public PersonBuilder phones(List<String> phones) {
        this.phones = phones;
        return this;
    }

    public PersonBuilder mobiles(String[] mobiles) {
        this.mobiles = mobiles;
        return this;
    }


    public PersonBuilder ignore() {
        this.ignore = "Just Ignore";
        return this;
    }

    public Person build() {
        return new Person(id, name, age, phones, ignore, mobiles);
    }
}