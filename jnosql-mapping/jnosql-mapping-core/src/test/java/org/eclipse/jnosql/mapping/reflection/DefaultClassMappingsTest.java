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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Vendor;
import jakarta.nosql.tck.entities.inheritance.EmailNotification;
import jakarta.nosql.tck.entities.inheritance.LargeProject;
import jakarta.nosql.tck.entities.inheritance.Notification;
import jakarta.nosql.tck.entities.inheritance.Project;
import jakarta.nosql.tck.entities.inheritance.SmallProject;
import jakarta.nosql.tck.entities.inheritance.SmsNotification;
import jakarta.nosql.tck.entities.inheritance.SocialMediaNotification;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;

@CDIExtension
class DefaultClassMappingsTest {

    @Inject
    private DefaultClassMappings mappings;

    @Test
    public void shouldGet(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings.get(Person.class);
        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.getClassInstance());
    }

    @Test
    public void shouldFindByName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings.findByName("vendors");
        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Vendor.class, mapping.getClassInstance());
    }
    @Test
    public void shouldFindBySimpleName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings
                .findBySimpleName(Person.class.getSimpleName())
                .orElseThrow();

        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.getClassInstance());
    }

    @Test
    public void shouldFindByClassName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings
                .findByClassName(Person.class.getName())
                .orElseThrow();

        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.getClassInstance());
    }

    @Test
    public void shouldLoadGetPriorityOnParent() {
        this.mappings.load(Notification.class);
        this.mappings.load(EmailNotification.class);
        this.mappings.load(SmsNotification.class);
        this.mappings.load(SocialMediaNotification.class);

        this.mappings.load(LargeProject.class);
        this.mappings.load(SmallProject.class);
        this.mappings.load(Project.class);


        EntityMetadata parent = this.mappings.findByName("Notification");
        Assertions.assertNotNull(parent);
        Assertions.assertEquals(Notification.class, parent.getClassInstance());

        parent = this.mappings.findByName("Project");
        Assertions.assertNotNull(parent);
        Assertions.assertEquals(Project.class, parent.getClassInstance());
    }

    @Test
    public void shouldFindByParentGroupByDiscriminatorValue() {
        Map<String, InheritanceClassMapping> group = this.mappings
                .findByParentGroupByDiscriminatorValue(Notification.class);

        Assertions.assertEquals(4, group.size());
        Assertions.assertNotNull(group.get("SocialMediaNotification"));
        Assertions.assertNotNull(group.get("SMS"));
        Assertions.assertNotNull(group.get("Email"));
    }

    @Test
    public void shouldFindByParentGroupByDiscriminatorValue2() {
        Map<String, InheritanceClassMapping> group = this.mappings
                .findByParentGroupByDiscriminatorValue(Project.class);

        Assertions.assertEquals(3, group.size());
        Assertions.assertNotNull(group.get("Small"));
        Assertions.assertNotNull(group.get("Large"));
        Assertions.assertNotNull(group.get("Project"));
    }
}