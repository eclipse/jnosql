/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph.spi;

import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.graph.BookRepository;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.graph.Transactional;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.eclipse.jnosql.mapping.DatabaseType.GRAPH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableAutoWeld
@AddPackages(value = {Converters.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
public class GraphConfigurationProducerExtensionTest {

    @Inject
    @Database(value = GRAPH, provider = "graphRepositoryMock")
    private GraphTemplate managerMock;

    @Inject
    private GraphTemplate manager;

    @Inject
    @Database(value = GRAPH, provider = "graphRepositoryMock")
    private BookRepository repositoryMock;

    @Inject
    @Database(value = GRAPH)
    private BookRepository repository;


    @Test
    public void shouldInstance() {
        assertNotNull(manager);
        assertNotNull(managerMock);
    }

    @Test
    public void shouldSave() {
        Person personMock = managerMock.insert(Person.builder().withId(10L).build());

        assertEquals(Long.valueOf(10L), personMock.getId());
    }

    @Test
    public void shouldInjectRepository() {
        repositoryMock.save(Book.builder().withName("book").build());
        assertNotNull(repository.findById("10"));
    }

}