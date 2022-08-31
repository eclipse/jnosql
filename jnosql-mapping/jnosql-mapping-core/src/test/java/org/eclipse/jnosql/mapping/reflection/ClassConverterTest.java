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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.mapping.MappingException;
import jakarta.nosql.tck.entities.Actor;
import jakarta.nosql.tck.entities.Director;
import jakarta.nosql.tck.entities.Machine;
import jakarta.nosql.tck.entities.NoConstructorEntity;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.User;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.entities.constructor.Computer;
import jakarta.nosql.tck.entities.inheritance.EmailNotification;
import jakarta.nosql.tck.entities.inheritance.Notification;
import jakarta.nosql.tck.entities.inheritance.Project;
import jakarta.nosql.tck.entities.inheritance.SmallProject;
import jakarta.nosql.tck.entities.inheritance.SocialMediaNotification;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static jakarta.nosql.mapping.DiscriminatorColumn.DEFAULT_DISCRIMINATOR_COLUMN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CDIExtension
public class ClassConverterTest {

    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldCreateEntityMetadata() {
        EntityMetadata entityMetadata = classConverter.create(Person.class);

        assertEquals("Person", entityMetadata.getName());
        assertEquals(Person.class, entityMetadata.getType());
        assertEquals(4, entityMetadata.getFields().size());
        assertThat(entityMetadata.getFieldsName(), containsInAnyOrder("_id", "name", "age", "phones"));
        ConstructorMetadata constructor = entityMetadata.getConstructor();
        assertNotNull(constructor);
        assertTrue(constructor.isDefault());

    }

    @Test
    public void shouldEntityMetadata2() {
        EntityMetadata entityMetadata = classConverter.create(Actor.class);

        assertEquals("Actor", entityMetadata.getName());
        assertEquals(Actor.class, entityMetadata.getType());
        assertEquals(6, entityMetadata.getFields().size());
        assertThat(entityMetadata.getFieldsName(), containsInAnyOrder("_id", "name", "age", "phones", "movieCharacter", "movieRating"));

    }

    @Test
    public void shouldCreateEntityMetadataWithEmbeddedClass() {
        EntityMetadata entityMetadata = classConverter.create(Director.class);
        assertEquals("Director", entityMetadata.getName());
        assertEquals(Director.class, entityMetadata.getType());
        assertEquals(5, entityMetadata.getFields().size());
        assertThat(entityMetadata.getFieldsName(), containsInAnyOrder("_id", "name", "age", "phones", "movie"));

    }

    @Test
    public void shouldReturnFalseWhenThereIsNotKey() {
        EntityMetadata entityMetadata = classConverter.create(Worker.class);
        boolean allMatch = entityMetadata.getFields().stream().noneMatch(FieldMapping::isId);
        assertTrue(allMatch);
    }


    @Test
    public void shouldReturnTrueWhenThereIsKey() {
        EntityMetadata entityMetadata = classConverter.create(User.class);
        List<FieldMapping> fields = entityMetadata.getFields();

        Predicate<FieldMapping> hasKeyAnnotation = FieldMapping::isId;
        assertTrue(fields.stream().anyMatch(hasKeyAnnotation));
        FieldMapping fieldMapping = fields.stream().filter(hasKeyAnnotation).findFirst().get();
        assertEquals("_id", fieldMapping.getName());
        assertEquals(MappingType.DEFAULT, fieldMapping.getType());

    }

    @Test
    public void shouldReturnErrorWhenThereIsNotConstructor() {
        Assertions.assertThrows(ConstructorException.class, () -> classConverter.create(NoConstructorEntity.class));
    }

    @Test
    public void shouldReturnWhenIsDefaultConstructor() {
        EntityMetadata entityMetadata = classConverter.create(Machine.class);
        List<FieldMapping> fields = entityMetadata.getFields();
        assertEquals(1, fields.size());
    }

    @Test
    public void shouldReturnEmptyInheritance() {
        EntityMetadata entityMetadata = classConverter.create(Person.class);
        Optional<InheritanceMetadata> inheritance = entityMetadata.getInheritance();
        Assertions.assertTrue(inheritance.isEmpty());
    }

    @Test
    public void shouldInheritance() {
        EntityMetadata entity = classConverter.create(SmallProject.class);
        Assertions.assertEquals(2, entity.getFields().size());
        Assertions.assertEquals(SmallProject.class, entity.getType());

        InheritanceMetadata inheritance = entity.getInheritance()
                .orElseThrow(MappingException::new);

        assertEquals("size", inheritance.getDiscriminatorColumn());
        assertEquals("Small", inheritance.getDiscriminatorValue());
        assertEquals(Project.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceNoDiscriminatorValue() {
        EntityMetadata entity = classConverter.create(SocialMediaNotification.class);
        Assertions.assertEquals(4, entity.getFields().size());
        Assertions.assertEquals(SocialMediaNotification.class, entity.getType());

        InheritanceMetadata inheritance = entity.getInheritance()
                .orElseThrow(MappingException::new);

        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, inheritance.getDiscriminatorColumn());
        assertEquals("SocialMediaNotification", inheritance.getDiscriminatorValue());
        assertEquals(Notification.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceNoDiscriminatorColumn() {
        EntityMetadata entity = classConverter.create(EmailNotification.class);
        Assertions.assertEquals(4, entity.getFields().size());
        Assertions.assertEquals(EmailNotification.class, entity.getType());

        InheritanceMetadata inheritance = entity.getInheritance()
                .orElseThrow(MappingException::new);

        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, inheritance.getDiscriminatorColumn());
        assertEquals("Email", inheritance.getDiscriminatorValue());
        assertEquals(Notification.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceSameParent() {
        EntityMetadata entity = classConverter.create(Project.class);
        Assertions.assertEquals(1, entity.getFields().size());
        Assertions.assertEquals(Project.class, entity.getType());

        InheritanceMetadata inheritance = entity.getInheritance()
                .orElseThrow(MappingException::new);

        assertEquals("size", inheritance.getDiscriminatorColumn());
        assertEquals("Project", inheritance.getDiscriminatorValue());
        assertEquals(Project.class, inheritance.getParent());
        assertEquals(Project.class, inheritance.getEntity());
    }


    @Test
    public void shouldCreateEntityMetadataWithConstructor() {
        EntityMetadata entityMetadata = classConverter.create(Computer.class);

        assertEquals("Computer", entityMetadata.getName());
        assertEquals(Computer.class, entityMetadata.getType());
        assertEquals(5, entityMetadata.getFields().size());
        assertThat(entityMetadata.getFieldsName(), containsInAnyOrder("_id", "name", "age", "model", "price"));
        ConstructorMetadata constructor = entityMetadata.getConstructor();
        assertNotNull(constructor);
        assertFalse(constructor.isDefault());
        assertEquals(5, constructor.getParameters().size());
    }

}