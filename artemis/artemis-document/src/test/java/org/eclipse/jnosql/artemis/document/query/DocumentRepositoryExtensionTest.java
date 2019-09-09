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
package org.eclipse.jnosql.artemis.document.query;

import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import org.eclipse.jnosql.artemis.CDIExtension;
import org.eclipse.jnosql.artemis.PersonRepository;
import org.eclipse.jnosql.artemis.PersonRepositoryAsync;
import org.eclipse.jnosql.artemis.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(CDIExtension.class)
public class DocumentRepositoryExtensionTest {


    @Inject
    @Database(value = DatabaseType.DOCUMENT)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.DOCUMENT, provider = "documentRepositoryMock")
    private PersonRepository repositoryMock;

    @Inject
    @Database(value = DatabaseType.DOCUMENT)
    private PersonRepositoryAsync repositoryAsync;

    @Inject
    @Database(value = DatabaseType.DOCUMENT, provider = "documentRepositoryMock")
    private PersonRepositoryAsync repositoryMockAsync;

    @Test
    public void shouldIniciateAsync() {
        assertNotNull(repositoryAsync);
    }

    @Test
    public void shouldGetQualifierAsync() {
        assertNotNull(repositoryMockAsync);
    }


    @Test
    public void shouldInitiate() {
        assertNotNull(repository);
        Person person = repository.save(Person.builder().build());
        assertEquals("Default", person.getName());
    }

    @Test
    public void shouldUseMock(){
        assertNotNull(repositoryMock);
        Person person = repositoryMock.save(Person.builder().build());
        assertEquals("documentRepositoryMock", person.getName());
    }
}