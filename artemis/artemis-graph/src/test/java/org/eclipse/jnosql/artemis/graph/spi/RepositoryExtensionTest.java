/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.graph.spi;

import jakarta.nosql.mapping.ConfigurationUnit;
import org.eclipse.jnosql.artemis.graph.BookRepository;
import org.eclipse.jnosql.artemis.graph.cdi.CDIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static jakarta.nosql.mapping.DatabaseType.GRAPH;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(CDIExtension.class)
public class RepositoryExtensionTest {

    @Inject
    @ConfigurationUnit(database = "database", fileName = "graph.json", name = "graphA")
    private BookRepository repositoryMock;


    @Inject
    @ConfigurationUnit(database = "database", fileName = "graph.json", name = "graphA", repository = GRAPH)
    private BookRepository repositoryMock2;

    @Test
    public void shouldUseUserRepository() {
        assertNotNull(repositoryMock);

    }

    @Test
    public void shouldUseUserRepository2() {
        assertNotNull(repositoryMock2);

    }
}
