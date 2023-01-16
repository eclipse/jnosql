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

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.mapping.test.entities.AnimalRepository;
import org.eclipse.jnosql.mapping.test.entities.Contact;
import org.eclipse.jnosql.mapping.test.entities.Job;
import org.eclipse.jnosql.mapping.test.entities.MovieRepository;
import org.eclipse.jnosql.mapping.test.entities.NoSQLVendor;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClassScannerTest {

    private ClassScanner classScanner = ClassScanner.INSTANCE;

    @Test
    public void shouldReturnEntities() {
        Set<Class<?>> entities = classScanner.entities();
        Assertions.assertNotNull(entities);
        assertThat(entities).hasSize(24)
                .contains(Person.class);
    }

    @Test
    public void shouldReturnEmbeddables() {
        Set<Class<?>> embeddables = classScanner.embeddables();
        Assertions.assertNotNull(embeddables);
        assertThat(embeddables).hasSize(4)
                .contains(Job.class, Contact.class);
    }


    @Test
    public void shouldReturnRepositories() {
        Set<Class<?>> reepositores = classScanner.repositories();
        Assertions.assertNotNull(reepositores);

        assertThat(reepositores).hasSize(3)
                .contains(AnimalRepository.class,
                        PersonRepository.class,
                        MovieRepository.class);
    }

    @Test
    public void shouldFilterRepositories() {
        Set<Class<?>> repositories = classScanner.repositories(NoSQLVendor.class);
        Assertions.assertNotNull(repositories);

        assertThat(repositories).hasSize(1)
                .contains(AnimalRepository.class);
    }

    @Test
    public void shouldFieldByCrudRepository() {
        Set<Class<?>> repositories = classScanner.repositories(CrudRepository.class);
        Assertions.assertNotNull(repositories);

        assertThat(repositories).hasSize(1)
                .contains(MovieRepository.class);
    }

    @Test
    public void shouldFieldByPageable() {
        Set<Class<?>> repositories = classScanner.repositories(PageableRepository.class);
        Assertions.assertNotNull(repositories);

        assertThat(repositories).hasSize(1)
                .contains(PersonRepository.class);
    }

    @Test
    public void shouldReturnStandardRepositories() {
        Set<Class<?>> repositories = classScanner.repositoriesStandard();
        assertThat(repositories).hasSize(2)
                .contains(PersonRepository.class, MovieRepository.class);
    }
}