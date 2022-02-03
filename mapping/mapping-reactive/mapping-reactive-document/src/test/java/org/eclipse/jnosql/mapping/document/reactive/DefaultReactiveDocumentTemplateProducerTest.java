/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.document.reactive;

import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class DefaultReactiveDocumentTemplateProducerTest {

    @Inject
    private ReactiveDocumentTemplateProducer producer;

    @Test
    public void shouldReturnTemplate() {
        final DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        final ReactiveDocumentTemplate reactiveTemplate = producer.get(template);
        assertNotNull(reactiveTemplate);
    }

    @Test
    public void shouldReturnNPEWhenTemplateIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> producer.get(null));
    }

}