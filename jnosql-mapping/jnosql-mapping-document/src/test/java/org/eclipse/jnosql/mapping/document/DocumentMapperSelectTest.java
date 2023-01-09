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
package org.eclipse.jnosql.mapping.document;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@CDIExtension
public class DocumentMapperSelectTest {

    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private DocumentManager managerMock;

    private DefaultDocumentTemplate template;


    private ArgumentCaptor<DocumentQuery> captor;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentManager.class);
        DocumentEventPersistManager persistManager = Mockito.mock(DocumentEventPersistManager.class);
        Instance<DocumentManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        DefaultDocumentWorkflow workflow = new DefaultDocumentWorkflow(persistManager, converter);
        this.template = new DefaultDocumentTemplate(converter, instance, workflow,
                persistManager, entities, converters);
    }


    @Test
    public void shouldReturnSelectStarFrom() {
        template.select(Person.class).getResult();
        DocumentQuery queryExpected = select().from("Person").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


}