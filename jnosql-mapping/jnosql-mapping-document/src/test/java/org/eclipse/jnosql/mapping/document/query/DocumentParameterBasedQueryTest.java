/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.data.Sort;
import jakarta.data.page.Pageable;
import jakarta.inject.Inject;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.document.MockProducer;
import org.eclipse.jnosql.mapping.document.entities.Person;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
class DocumentParameterBasedQueryTest {


    @Inject
    private EntitiesMetadata entitiesMetadata;

    private EntityMetadata metadata;
    @BeforeEach
    void setUp(){
        this.metadata = entitiesMetadata.get(Person.class);
    }

    @Test
    void shouldCreateQuerySingleParameter(){
        Map<String, Object> params = Map.of("name", "Ada");
        var query = DocumentParameterBasedQuery.INSTANCE.toQuery(params, null, metadata);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(query.limit()).isEqualTo(0L);
            soft.assertThat(query.skip()).isEqualTo(0L);
            soft.assertThat(query.name()).isEqualTo("Person");
            soft.assertThat(query.sorts()).isEmpty();
            soft.assertThat(query.condition()).isNotEmpty();
            soft.assertThat(query.condition()).get().isEqualTo(DocumentCondition.eq(Document.of("name", "Ada")));
        });
    }

    @Test
    void shouldCreateQueryMultipleParams(){
        Map<String, Object> params = Map.of("name", "Ada", "age", 10);
        var query = DocumentParameterBasedQuery.INSTANCE.toQuery(params, null, metadata);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(query.limit()).isEqualTo(0L);
            soft.assertThat(query.skip()).isEqualTo(0L);
            soft.assertThat(query.name()).isEqualTo("Person");
            soft.assertThat(query.sorts()).isEmpty();
            soft.assertThat(query.condition()).isNotEmpty();
            var condition = query.condition().orElseThrow();
            soft.assertThat(condition.condition()).isEqualTo(Condition.AND);
            soft.assertThat(condition.document().get(new TypeReference<List<DocumentCondition>>() {
            })).contains(DocumentCondition.eq(Document.of("name", "Ada")),
                    DocumentCondition.eq(Document.of("age", 10)));
        });

    }

    @Test
    void shouldCreateQueryEmptyParams(){
        Map<String, Object> params = Collections.emptyMap();
        var query = DocumentParameterBasedQuery.INSTANCE.toQuery(params, null, metadata);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(query.limit()).isEqualTo(0L);
            soft.assertThat(query.skip()).isEqualTo(0L);
            soft.assertThat(query.name()).isEqualTo("Person");
            soft.assertThat(query.sorts()).isEmpty();
            soft.assertThat(query.condition()).isEmpty();
        });
    }

    @Test
    void shouldCreateQueryPageable(){
        Map<String, Object> params = Map.of("name", "Ada");
        var pageable = Pageable.ofPage(2).size(10).sortBy(Sort.asc("name"));
        var query = DocumentParameterBasedQuery.INSTANCE.toQuery(params, pageable, metadata);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(query.name()).isEqualTo("Person");
            soft.assertThat(query.condition()).isNotEmpty();
            soft.assertThat(query.condition()).get().isEqualTo(DocumentCondition.eq(Document.of("name", "Ada")));
            soft.assertThat(query.limit()).isEqualTo(10L);
            soft.assertThat(query.skip()).isEqualTo(10L);
            soft.assertThat(query.sorts()).hasSize(1).contains(Sort.asc("name"));
        });
    }
}