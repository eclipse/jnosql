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
package org.eclipse.jnosql.mapping.graph;


import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.graph.entities.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.graph.entities.inheritance.Notification;
import org.eclipse.jnosql.mapping.graph.entities.inheritance.SmsNotification;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@EnableAutoWeld
@AddPackages(value = {Converters.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class GraphTemplateInheritanceTest {


    @Inject
    private GraphTemplate graphTemplate;

    @Inject
    private Graph graph;

    protected Graph getGraph() {
        return graph;
    }


    @AfterEach
    void after() {
        getGraph().traversal().V().toList().forEach(Vertex::remove);
        getGraph().traversal().E().toList().forEach(Edge::remove);
    }


    @Test
    void shouldSelectFindAllFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var notifications = graphTemplate.findAll(EmailNotification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(1)
                .containsExactly(emailNotification));

    }

    @Test
    void shouldSelectFindAllNoFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var notifications = graphTemplate.findAll(Notification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(2)
                .containsExactly(emailNotification, smsNotification));

    }

    @Test
    void shouldDeleteAllFilter(){

        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));

        graphTemplate.deleteAll(EmailNotification.class);

        var notifications = graphTemplate.findAll(Notification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(1)
                .containsExactly(smsNotification));

    }

    @Test
    void shouldDeleteAllNoFilter(){

        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));

        graphTemplate.deleteAll(Notification.class);

        var notifications = graphTemplate.findAll(Notification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications).isEmpty());

    }

    @Test
    void shouldCountAllFilter(){

        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var count = graphTemplate.count(EmailNotification.class);
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldCountAlNolFilter(){

        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var count = graphTemplate.count(Notification.class);
        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldSelectFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var notifications = graphTemplate.select(EmailNotification.class).stream().toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(1)
                .containsExactly(emailNotification));

    }

    @Test
    void shouldSelectNoFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));
        var notifications = graphTemplate.select(Notification.class).stream().toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(2)
                .containsExactly(emailNotification, smsNotification));

    }

    @Test
    void shouldDeleteFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));

        graphTemplate.delete(EmailNotification.class).execute();

        var notifications = graphTemplate.findAll(Notification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications)
                .hasSize(1)
                .containsExactly(smsNotification));
    }

    @Test
    void shouldDeleteNoFilter(){
        var emailNotification = new EmailNotification();
        emailNotification.setName("email");

        var smsNotification = new SmsNotification();
        smsNotification.setName("notification");

        this.graphTemplate.insert(List.of(emailNotification, smsNotification));

        graphTemplate.delete(Notification.class).execute();

        var notifications = graphTemplate.findAll(Notification.class).toList();
        assertSoftly(soft -> soft.assertThat(notifications).isEmpty());
    }
}
