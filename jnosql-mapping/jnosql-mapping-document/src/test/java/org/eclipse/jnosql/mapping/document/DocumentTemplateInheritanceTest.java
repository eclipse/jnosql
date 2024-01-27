/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.document.entities.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.document.entities.inheritance.Notification;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
class DocumentTemplateInheritanceTest {


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private DocumentManager managerMock;

    private DefaultDocumentTemplate template;



    private DocumentEventPersistManager documentEventPersistManager;

    @BeforeEach
    void setUp() {
        managerMock = Mockito.mock(DocumentManager.class);
        documentEventPersistManager = Mockito.mock(DocumentEventPersistManager.class);

        Instance<DocumentManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        this.template = new DefaultDocumentTemplate(converter, instance,
                documentEventPersistManager, entities, converters);
    }

    @Test
    void shouldSelectFilter(){
        var captor = ArgumentCaptor.forClass(DocumentQuery.class);
        template.select(EmailNotification.class).<EmailNotification>stream().toList();
        Mockito.verify(this.managerMock).select(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(Condition.EQUALS);
            soft.assertThat(condition.document()).isEqualTo(Document.of("dtype", "Email"));
        });
    }

    @Test
    void shouldSelectNoFilter(){
        var captor = ArgumentCaptor.forClass(DocumentQuery.class);
        template.select(Notification.class).<Notification>stream().toList();
        Mockito.verify(this.managerMock).select(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isEmpty();
        });
    }

    @Test
    void shouldDeleteFilter(){
        var captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        template.delete(EmailNotification.class).execute();
        Mockito.verify(this.managerMock).delete(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(Condition.EQUALS);
            soft.assertThat(condition.document()).isEqualTo(Document.of("dtype", "Email"));
        });
    }

    @Test
    void shouldDeleteNoFilter(){
        var captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        template.delete(Notification.class).execute();
        Mockito.verify(this.managerMock).delete(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isEmpty();
        });
    }

    @Test
    void shouldSelectFilterCondition(){
        var captor = ArgumentCaptor.forClass(DocumentQuery.class);
        template.select(EmailNotification.class).where("name")
                .eq("notification").<EmailNotification>stream().toList();
        Mockito.verify(this.managerMock).select(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(Condition.AND);
            var documents = condition.document().get(new TypeReference<List<DocumentCondition>>() {});
            soft.assertThat(documents).contains(DocumentCondition.eq(Document.of("dtype", "Email")),
                    DocumentCondition.eq(Document.of("name", "notification")));
        });
    }

    @Test
    void shouldDeleteFilterCondition(){
        var captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        template.delete(EmailNotification.class).where("name")
                .eq("notification").execute();
        Mockito.verify(this.managerMock).delete(captor.capture());
        var query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(Condition.AND);
            var documents = condition.document().get(new TypeReference<List<DocumentCondition>>() {});
            soft.assertThat(documents).contains(DocumentCondition.eq(Document.of("dtype", "Email")),
                    DocumentCondition.eq(Document.of("name", "notification")));
        });
    }

}
