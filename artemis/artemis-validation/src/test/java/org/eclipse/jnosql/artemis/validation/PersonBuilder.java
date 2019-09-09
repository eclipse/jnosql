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
package org.eclipse.jnosql.artemis.validation;

import java.math.BigDecimal;
import java.util.List;

public class PersonBuilder {

    private String name;

    private Integer age;

    private BigDecimal salary;

    private List<String> phones;

    public PersonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder withAge(Integer age) {
        this.age = age;
        return this;
    }

    public PersonBuilder withSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }

    public PersonBuilder withPhones(List<String> phones) {
        this.phones = phones;
        return this;
    }

    public Person build() {
        return new Person(name, age, salary, phones);
    }
}