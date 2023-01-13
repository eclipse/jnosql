/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities;

import java.util.List;

public class PersonBuilder {
    private long id;
    private String name;
    private int age;
    private List<String> phones;
    private String ignore;

    public PersonBuilder withId(long id) {
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