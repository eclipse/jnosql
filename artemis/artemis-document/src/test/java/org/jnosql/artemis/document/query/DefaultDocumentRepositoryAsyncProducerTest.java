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
package org.jnosql.artemis.document.query;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.PersonRepositoryAsync;
import org.jnosql.artemis.document.DocumentRepositoryAsyncProducer;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(CDIExtension.class)
class DefaultDocumentRepositoryAsyncProducerTest {

    @Inject
    private DocumentRepositoryAsyncProducer producer;


    @Test
    public void shouldCreateFromManager() {
        DocumentCollectionManagerAsync manager= Mockito.mock(DocumentCollectionManagerAsync.class);
        PersonRepositoryAsync personRepository = producer.get(PersonRepositoryAsync.class, manager);
        assertNotNull(personRepository);
    }


    @Test
    public void shouldCreateFromTemplate() {
        DocumentTemplateAsync template= Mockito.mock(DocumentTemplateAsync.class);
        PersonRepositoryAsync personRepository = producer.get(PersonRepositoryAsync.class, template);
        assertNotNull(personRepository);
    }
}