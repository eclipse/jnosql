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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.data.repository.CrudRepository;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.document.MockProducer;
import org.eclipse.jnosql.mapping.document.entities.inheritance.EmailNotification;
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

import java.lang.reflect.Proxy;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.mockito.Mockito.verify;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
class DocumentCrudInheritanceRepositoryProxyTest {

    private JNoSQLDocumentTemplate template;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private EmailRepository emailRepository;



    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(JNoSQLDocumentTemplate.class);

        DocumentRepositoryProxy<EmailNotification, Long> personHandler = new DocumentRepositoryProxy<>(template,
                entities, EmailRepository.class, converters);


        emailRepository = (EmailRepository) Proxy.newProxyInstance(EmailRepository.class.getClassLoader(),
                new Class[]{EmailRepository.class},
                personHandler);
    }

    @Test
    void shouldPutFilterAtFindAll() {
        emailRepository.findAll();
        verify(template).findAll(EmailNotification.class);
    }


    @Test
    void shouldPutFilterAtCount() {
        emailRepository.countBy();
        Mockito.verify(template).count(EmailNotification.class);
    }

    @Test
    void shouldPutFilterAtFindByName() {
        emailRepository.findByName("name");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Mockito.verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(AND);
            var conditions = condition.document().get(new TypeReference<List<DocumentCondition>>() {
            });
            soft.assertThat(conditions).hasSize(2).contains(DocumentCondition.eq(Document.of("dtype", "Email")));
        });
    }

    @Test
    void shouldPutFilterAtCountByName() {
        emailRepository.countByName("name");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Mockito.verify(template).count(captor.capture());
        DocumentQuery query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(AND);
            var conditions = condition.document().get(new TypeReference<List<DocumentCondition>>() {
            });
            soft.assertThat(conditions).hasSize(2).contains(DocumentCondition.eq(Document.of("dtype", "Email")));
        });
    }


    @Test
    void shouldPutFilterAtExistByName() {
        emailRepository.existsByName("name");
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Mockito.verify(template).exists(captor.capture());
        DocumentQuery query = captor.getValue();
        assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Notification");
            soft.assertThat(query.condition()).isPresent();
            DocumentCondition condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(AND);
            var conditions = condition.document().get(new TypeReference<List<DocumentCondition>>() {
            });
            soft.assertThat(conditions).hasSize(2).contains(DocumentCondition.eq(Document.of("dtype", "Email")));
        });
    }



    public interface EmailRepository extends CrudRepository<EmailNotification, String> {

        List<EmailNotification> findByName(String name);

        long countByName(String name);

        boolean existsByName(String name);


    }


}
