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

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.document.DocumentRepositoryProducer;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.entities.PersonRepository;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class DefaultDocumentRepositoryProducerTest {

    @Inject
    private DocumentRepositoryProducer producer;


    @Test
    public void shouldCreateFromManager() {
        DocumentCollectionManager manager= Mockito.mock(DocumentCollectionManager.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, manager);
        assertNotNull(personRepository);
    }


    @Test
    public void shouldCreateFromTemplate() {
        DocumentTemplate template= Mockito.mock(DocumentTemplate.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, template);
        assertNotNull(personRepository);
    }
}