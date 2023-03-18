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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.data.repository.PageableRepository;
import jakarta.nosql.Column;
import jakarta.nosql.Id;
import org.eclipse.jnosql.mapping.test.entities.NoSQLVendor;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class UnsupportedRepositoryFilterTest {

    private Predicate<Class<?>> predicate;

    @BeforeEach
    public void setUp() {
        this.predicate = UnsupportedRepositoryFilter.INSTANCE;
    }

    @Test
    public void shouldReturnFalseWhenHasSupportRepository() {
        assertThat(predicate.test(PersonRepository.class)).isFalse();
        assertThat(predicate.test(People.class)).isFalse();
        assertThat(predicate.test(Persons.class)).isFalse();

    }

    @Test
    public void shouldReturnTrueWhenHasSupportRepository() {
        assertThat(predicate.test(NoSQLVendor.class)).isTrue();
        assertThat(predicate.test(Server.class)).isTrue();
        assertThat(predicate.test(StringSupplier.class)).isTrue();
        assertThat(predicate.test(Repository.class)).isTrue();
    }


    private interface People extends PageableRepository<Person, Long> {

    }

    private interface Persons extends PageableRepository<Person, Long> {

    }

    private interface Server extends PageableRepository<Computer, String> {

    }

    private interface StringSupplier extends Supplier<String> {

    }

    private interface Repository {

    }

    private class Computer {

        @Id
        private String id;

        @Column
        private String name;
    }
}