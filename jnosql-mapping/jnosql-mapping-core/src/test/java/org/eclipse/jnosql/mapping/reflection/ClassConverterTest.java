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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.test.entities.Actor;
import org.eclipse.jnosql.mapping.test.entities.Director;
import org.eclipse.jnosql.mapping.test.entities.Machine;
import org.eclipse.jnosql.mapping.test.entities.NoConstructorEntity;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.User;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.eclipse.jnosql.mapping.test.entities.constructor.Computer;
import org.eclipse.jnosql.mapping.test.entities.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Notification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Project;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SmallProject;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SocialMediaNotification;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.eclipse.jnosql.mapping.DiscriminatorColumn.DEFAULT_DISCRIMINATOR_COLUMN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)
@AddExtensions(EntityMetadataExtension.class)
public class ClassConverterTest {

    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldCreateEntityMetadata() {
        EntityMetadata entityMetadata = classConverter.create(Person.class);

        assertEquals("Person", entityMetadata.name());
        assertEquals(Person.class, entityMetadata.type());
        assertEquals(4, entityMetadata.fields().size());
        assertThat(entityMetadata.fieldsName()).contains("_id", "name", "age", "phones");
        ConstructorMetadata constructor = entityMetadata.constructor();
        assertNotNull(constructor);
        assertTrue(constructor.isDefault());

    }

    @Test
    public void shouldEntityMetadata2() {
        EntityMetadata entityMetadata = classConverter.create(Actor.class);

        assertEquals("Actor", entityMetadata.name());
        assertEquals(Actor.class, entityMetadata.type());

        assertThat(entityMetadata.fieldsName())
                .hasSize(6)
                .contains("_id", "name", "age", "phones", "movieCharacter", "movieRating");

    }

    @Test
    public void shouldCreateEntityMetadataWithEmbeddedClass() {
        EntityMetadata entityMetadata = classConverter.create(Director.class);
        assertEquals("Director", entityMetadata.name());
        assertEquals(Director.class, entityMetadata.type());
        assertEquals(5, entityMetadata.fields().size());
        assertThat(entityMetadata.fieldsName()).contains("_id", "name", "age", "phones", "movie");

    }

    @Test
    public void shouldReturnFalseWhenThereIsNotKey() {
        EntityMetadata entityMetadata = classConverter.create(Worker.class);
        boolean allMatch = entityMetadata.fields().stream().noneMatch(FieldMapping::isId);
        assertTrue(allMatch);
    }


    @Test
    public void shouldReturnTrueWhenThereIsKey() {
        EntityMetadata entityMetadata = classConverter.create(User.class);
        List<FieldMapping> fields = entityMetadata.fields();

        Predicate<FieldMapping> hasKeyAnnotation = FieldMapping::isId;
        assertTrue(fields.stream().anyMatch(hasKeyAnnotation));
        FieldMapping fieldMapping = fields.stream().filter(hasKeyAnnotation).findFirst().get();
        assertEquals("_id", fieldMapping.name());
        assertEquals(MappingType.DEFAULT, fieldMapping.type());

    }

    @Test
    public void shouldReturnErrorWhenThereIsNotConstructor() {
        Assertions.assertThrows(ConstructorException.class, () -> classConverter.create(NoConstructorEntity.class));
    }

    @Test
    public void shouldReturnWhenIsDefaultConstructor() {
        EntityMetadata entityMetadata = classConverter.create(Machine.class);
        List<FieldMapping> fields = entityMetadata.fields();
        assertEquals(1, fields.size());
    }

    @Test
    public void shouldReturnEmptyInheritance() {
        EntityMetadata entityMetadata = classConverter.create(Person.class);
        Optional<InheritanceMetadata> inheritance = entityMetadata.inheritance();
        Assertions.assertTrue(inheritance.isEmpty());
    }

    @Test
    public void shouldInheritance() {
        EntityMetadata entity = classConverter.create(SmallProject.class);
        Assertions.assertEquals(2, entity.fields().size());
        Assertions.assertEquals(SmallProject.class, entity.type());

        InheritanceMetadata inheritance = entity.inheritance()
                .orElseThrow(RuntimeException::new);

        assertEquals("size", inheritance.getDiscriminatorColumn());
        assertEquals("Small", inheritance.getDiscriminatorValue());
        assertEquals(Project.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceNoDiscriminatorValue() {
        EntityMetadata entity = classConverter.create(SocialMediaNotification.class);
        Assertions.assertEquals(4, entity.fields().size());
        Assertions.assertEquals(SocialMediaNotification.class, entity.type());

        InheritanceMetadata inheritance = entity.inheritance()
                .orElseThrow(RuntimeException::new);

        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, inheritance.getDiscriminatorColumn());
        assertEquals("SocialMediaNotification", inheritance.getDiscriminatorValue());
        assertEquals(Notification.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceNoDiscriminatorColumn() {
        EntityMetadata entity = classConverter.create(EmailNotification.class);
        Assertions.assertEquals(4, entity.fields().size());
        Assertions.assertEquals(EmailNotification.class, entity.type());

        InheritanceMetadata inheritance = entity.inheritance()
                .orElseThrow(RuntimeException::new);

        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, inheritance.getDiscriminatorColumn());
        assertEquals("Email", inheritance.getDiscriminatorValue());
        assertEquals(Notification.class, inheritance.getParent());
    }

    @Test
    public void shouldInheritanceSameParent() {
        EntityMetadata entity = classConverter.create(Project.class);
        Assertions.assertEquals(1, entity.fields().size());
        Assertions.assertEquals(Project.class, entity.type());

        InheritanceMetadata inheritance = entity.inheritance()
                .orElseThrow(RuntimeException::new);

        assertEquals("size", inheritance.getDiscriminatorColumn());
        assertEquals("Project", inheritance.getDiscriminatorValue());
        assertEquals(Project.class, inheritance.getParent());
        assertEquals(Project.class, inheritance.getEntity());
    }


    @Test
    public void shouldCreateEntityMetadataWithConstructor() {
        EntityMetadata entityMetadata = classConverter.create(Computer.class);

        assertEquals("Computer", entityMetadata.name());
        assertEquals(Computer.class, entityMetadata.type());
        assertEquals(5, entityMetadata.fields().size());
        assertThat(entityMetadata.fieldsName()).contains("_id", "name", "age", "model", "price");
        ConstructorMetadata constructor = entityMetadata.constructor();
        assertNotNull(constructor);
        assertFalse(constructor.isDefault());
        assertEquals(5, constructor.getParameters().size());
    }

}