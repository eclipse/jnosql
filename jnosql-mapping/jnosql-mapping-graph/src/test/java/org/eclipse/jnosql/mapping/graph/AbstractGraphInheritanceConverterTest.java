/*
 *  Copyright (c) 2022 Otávio Santana and others
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

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.model.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.graph.model.inheritance.LargeProject;
import org.eclipse.jnosql.mapping.graph.model.inheritance.Project;
import org.eclipse.jnosql.mapping.graph.model.inheritance.SmallProject;
import org.eclipse.jnosql.mapping.graph.model.inheritance.SmsNotification;
import org.eclipse.jnosql.mapping.graph.model.inheritance.SocialMediaNotification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractGraphInheritanceConverterTest {

    protected abstract Graph getGraph();

    protected abstract GraphConverter getConverter();

    @BeforeEach
    public void setUp() {
        getGraph().traversal().V().toList().forEach(Vertex::remove);
        getGraph().traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldConvertProjectToSmallProject() {
        Vertex vertex = getGraph().addVertex(T.label, "Project",
                "investor", "Otavio Santana",
                "size", "Small", "name",
                "Small Project");
        Project project = this.getConverter().toEntity(vertex);
        assertEquals("Small Project", project.getName());
        assertEquals(SmallProject.class, project.getClass());
        SmallProject smallProject = SmallProject.class.cast(project);
        assertEquals("Otavio Santana", smallProject.getInvestor());
    }

    @Test
    public void shouldConvertProjectToLargeProject() {
        Vertex vertex = getGraph().addVertex(T.label, "Project",
                "investor", "Otavio Santana",
                "size", "Large",
                "name", "Large Project",
                "budget", BigDecimal.TEN);

        Project project = this.getConverter().toEntity(vertex);
        assertEquals("Large Project", project.getName());
        assertEquals(LargeProject.class, project.getClass());
        LargeProject smallProject = LargeProject.class.cast(project);
        assertEquals(BigDecimal.TEN, smallProject.getBudget());
    }

//    @Test
//    public void shouldConvertLargeProjectToCommunicationEntity() {
//        LargeProject project = new LargeProject();
//        project.setName("Large Project");
//        project.setBudget(BigDecimal.TEN);
//        DocumentEntity entity = this.converter.toDocument(project);
//        assertNotNull(entity);
//        assertEquals("Project", entity.getName());
//        assertEquals(project.getName(), entity.find("_id", String.class).get());
//        assertEquals(project.getBudget(), entity.find("budget", BigDecimal.class).get());
//        assertEquals("Large", entity.find("size", String.class).get());
//    }
//
//    @Test
//    public void shouldConvertSmallProjectToCommunicationEntity() {
//        SmallProject project = new SmallProject();
//        project.setName("Small Project");
//        project.setInvestor("Otavio Santana");
//        DocumentEntity entity = this.converter.toDocument(project);
//        assertNotNull(entity);
//        assertEquals("Project", entity.getName());
//        assertEquals(project.getName(), entity.find("_id", String.class).get());
//        assertEquals(project.getInvestor(), entity.find("investor", String.class).get());
//        assertEquals("Small", entity.find("size", String.class).get());
//    }

    @Test
    public void shouldConvertDocumentEntityToSocialMedia(){
        LocalDate date = LocalDate.now();
        Vertex vertex = getGraph().addVertex(T.label, "Notification",
                "name", "Social Media",
                "nickname", "otaviojava",
                "type", SocialMediaNotification.class.getSimpleName(),
                "createdOn",date,
                "budget", BigDecimal.TEN);

        SocialMediaNotification notification = this.getConverter().toEntity(vertex);
        assertEquals(100L, notification.getId());
        assertEquals("Social Media", notification.getName());
        assertEquals("otaviojava", notification.getNickname());
        assertEquals(date, notification.getCreatedOn());
    }

    @Test
    public void shouldConvertDocumentEntityToSms(){
        LocalDate date = LocalDate.now();

        Vertex vertex = getGraph().addVertex(T.label, "Notification",
                "name", "SMS Notification",
                "phone", "+351987654123",
                "type", "SMS",
                "createdOn",date,
                "budget", BigDecimal.TEN);

        SmsNotification notification = this.getConverter().toEntity(vertex);
        Assertions.assertEquals(100L, notification.getId());
        Assertions.assertEquals("SMS Notification", notification.getName());
        Assertions.assertEquals("+351987654123", notification.getPhone());
        assertEquals(date, notification.getCreatedOn());
    }

    @Test
    public void shouldConvertDocumentEntityToEmail(){
        LocalDate date = LocalDate.now();

        Vertex vertex = getGraph().addVertex(T.label, "Notification",
                "name", "SMS Notification",
                "email", "otavio@otavio.test",
                "type", "Email",
                "createdOn",date,
                "budget", BigDecimal.TEN);

        EmailNotification notification = this.getConverter().toEntity(vertex);
        Assertions.assertEquals(100L, notification.getId());
        Assertions.assertEquals("Email Notification", notification.getName());
        Assertions.assertEquals("otavio@otavio.test", notification.getEmail());
        assertEquals(date, notification.getCreatedOn());
    }

//    @Test
//    public void shouldConvertSocialMediaToCommunicationEntity(){
//        SocialMediaNotification notification = new SocialMediaNotification();
//        notification.setId(100L);
//        notification.setName("Social Media");
//        notification.setCreatedOn(LocalDate.now());
//        notification.setNickname("otaviojava");
//        DocumentEntity entity = this.converter.toDocument(notification);
//        assertNotNull(entity);
//        assertEquals("Notification", entity.getName());
//        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
//        assertEquals(notification.getName(), entity.find("name", String.class).get());
//        assertEquals(notification.getNickname(), entity.find("nickname", String.class).get());
//        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
//    }
//
//    @Test
//    public void shouldConvertSmsToCommunicationEntity(){
//        SmsNotification notification = new SmsNotification();
//        notification.setId(100L);
//        notification.setName("SMS");
//        notification.setCreatedOn(LocalDate.now());
//        notification.setPhone("+351123456987");
//        DocumentEntity entity = this.converter.toDocument(notification);
//        assertNotNull(entity);
//        assertEquals("Notification", entity.getName());
//        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
//        assertEquals(notification.getName(), entity.find("name", String.class).get());
//        assertEquals(notification.getPhone(), entity.find("phone", String.class).get());
//        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
//    }
//
//    @Test
//    public void shouldConvertEmailToCommunicationEntity(){
//        EmailNotification notification = new EmailNotification();
//        notification.setId(100L);
//        notification.setName("Email Media");
//        notification.setCreatedOn(LocalDate.now());
//        notification.setEmail("otavio@otavio.test.com");
//        DocumentEntity entity = this.converter.toDocument(notification);
//        assertNotNull(entity);
//        assertEquals("Notification", entity.getName());
//        assertEquals(notification.getId(), entity.find("_id", Long.class).get());
//        assertEquals(notification.getName(), entity.find("name", String.class).get());
//        assertEquals(notification.getEmail(), entity.find("email", String.class).get());
//        assertEquals(notification.getCreatedOn(), entity.find("createdOn", LocalDate.class).get());
//    }
//
//    @Test
//    public void shouldReturnErrorWhenConvertMissingColumn(){
//        LocalDate date = LocalDate.now();
//        DocumentEntity entity = DocumentEntity.of("Notification");
//        entity.add("_id", 100L);
//        entity.add("name", "SMS Notification");
//        entity.add("phone", "+351987654123");
//        entity.add("createdOn", date);
//        Assertions.assertThrows(MappingException.class, ()-> this.converter.toEntity(entity));
//    }
//
//    @Test
//    public void shouldReturnErrorWhenMismatchField() {
//        LocalDate date = LocalDate.now();
//        DocumentEntity entity = DocumentEntity.of("Notification");
//        entity.add("_id", 100L);
//        entity.add("name", "Email Notification");
//        entity.add("email", "otavio@otavio.test");
//        entity.add("createdOn", date);
//        entity.add("type", "Wrong");
//        Assertions.assertThrows(MappingException.class, ()-> this.converter.toEntity(entity));
//    }
}
