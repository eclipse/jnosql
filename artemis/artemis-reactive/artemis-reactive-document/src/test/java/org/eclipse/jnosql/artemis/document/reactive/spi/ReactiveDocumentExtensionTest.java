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
package org.eclipse.jnosql.artemis.document.reactive.spi;

import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.artemis.document.reactive.ReactiveDocumentTemplate;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@CDIExtension
public class ReactiveDocumentExtensionTest {


    @Inject
    @Database(value = DatabaseType.DOCUMENT)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.DOCUMENT, provider = "documentRepositoryMock")
    private PersonRepository repositoryMock;

    @Inject
    private ReactiveDocumentTemplate template;

    @Inject
    @Database(value = DatabaseType.DOCUMENT, provider = "documentRepositoryMock")
    private ReactiveDocumentTemplate templateMock;

    @Test
    public void shouldInjectRepository() {
        assertNotNull(repository);
        assertNotNull(repositoryMock);
    }

    @Test
    public void shouldTemplateRepository(){
        assertNotNull(template);
        assertNotNull(templateMock);
    }
}