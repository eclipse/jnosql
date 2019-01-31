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
package org.jnosql.artemis.document;

import org.jnosql.artemis.EntityPostPersit;
import org.jnosql.artemis.EntityPrePersist;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.event.Event;

import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.delete;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultDocumentEventPersistManagerTest {

    @InjectMocks
    private DefaultDocumentEventPersistManager subject;

    @Mock
    private Event<DocumentEntityPrePersist> documentEntityPrePersistEvent;

    @Mock
    private Event<DocumentEntityPostPersist> documentEntityPostPersistEvent;

    @Mock
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Mock
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Mock
    private Event<EntityDocumentPrePersist> entityDocumentPrePersist;

    @Mock
    private Event<EntityDocumentPostPersist> entityDocumentPostPersist;

    @Mock
    private Event<DocumentQueryExecute> documentQueryExecute;

    @Mock
    private Event<DocumentDeleteQueryExecute> documentDeleteQueryExecute;


    @Test
    public void shouldFirePreDocument() {
        DocumentEntity entity = DocumentEntity.of("collection");
        subject.firePreDocument(entity);
        ArgumentCaptor<DocumentEntityPrePersist> captor = ArgumentCaptor.forClass(DocumentEntityPrePersist.class);
        verify(documentEntityPrePersistEvent).fire(captor.capture());

        DocumentEntityPrePersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }


    @Test
    public void shouldFirePostDocument() {
        DocumentEntity entity = DocumentEntity.of("collection");
        subject.firePostDocument(entity);
        ArgumentCaptor<DocumentEntityPostPersist> captor = ArgumentCaptor.forClass(DocumentEntityPostPersist.class);
        verify(documentEntityPostPersistEvent).fire(captor.capture());

        DocumentEntityPostPersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }

    @Test
    public void shouldFirePreEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreEntity(jedi);
        ArgumentCaptor<EntityPrePersist> captor = ArgumentCaptor.forClass(EntityPrePersist.class);
        verify(entityPrePersistEvent).fire(captor.capture());
        EntityPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostEntity(jedi);
        ArgumentCaptor<EntityPostPersit> captor = ArgumentCaptor.forClass(EntityPostPersit.class);
        verify(entityPostPersitEvent).fire(captor.capture());
        EntityPostPersit value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }
    //

    @Test
    public void shouldFirePreDocumentEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreDocumentEntity(jedi);
        ArgumentCaptor<EntityDocumentPrePersist> captor = ArgumentCaptor.forClass(EntityDocumentPrePersist.class);
        verify(entityDocumentPrePersist).fire(captor.capture());
        EntityDocumentPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostDocumentEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostDocumentEntity(jedi);
        ArgumentCaptor<EntityDocumentPostPersist> captor = ArgumentCaptor.forClass(EntityDocumentPostPersist.class);
        verify(entityDocumentPostPersist).fire(captor.capture());
        EntityDocumentPostPersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePreQuery() {

        DocumentQuery query = select().from("collection").build();
        subject.firePreQuery(query);
        ArgumentCaptor<DocumentQueryExecute> captor = ArgumentCaptor.forClass(DocumentQueryExecute.class);
        verify(documentQueryExecute).fire(captor.capture());
        assertEquals(query, captor.getValue().getQuery());
    }

    @Test
    public void shouldFirePreDeleteQuery() {
        DocumentDeleteQuery query = delete().from("collection").build();
        subject.firePreDeleteQuery(query);
        ArgumentCaptor<DocumentDeleteQueryExecute> captor = ArgumentCaptor.forClass(DocumentDeleteQueryExecute.class);
        verify(documentDeleteQueryExecute).fire(captor.capture());
        assertEquals(query, captor.getValue().getQuery());
    }


    class Jedi {
        private String name;
    }

}