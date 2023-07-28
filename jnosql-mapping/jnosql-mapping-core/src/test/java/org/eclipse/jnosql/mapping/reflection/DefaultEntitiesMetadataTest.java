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

import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.test.entities.Movie;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Vendor;
import org.eclipse.jnosql.mapping.test.entities.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.LargeProject;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Notification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Project;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SmallProject;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SmsNotification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SocialMediaNotification;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)
@AddExtensions(EntityMetadataExtension.class)
class DefaultEntitiesMetadataTest {

    @Inject
    private DefaultEntitiesMetadata mappings;

    @Test
    public void shouldGet(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings.get(Person.class);
        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.type());
    }

    @Test
    public void shouldFindByName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings.findByName("vendors");
        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Vendor.class, mapping.type());
    }
    @Test
    public void shouldFindBySimpleName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings
                .findBySimpleName(Person.class.getSimpleName())
                .orElseThrow();

        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.type());
    }

    @Test
    public void shouldFindByClassName(){
        this.mappings.load(Person.class);
        this.mappings.load(Vendor.class);

        EntityMetadata mapping = this.mappings
                .findByClassName(Person.class.getName())
                .orElseThrow();

        Assertions.assertNotNull(mapping);
        Assertions.assertEquals(Person.class, mapping.type());
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
        Assertions.assertEquals(Notification.class, parent.type());

        parent = this.mappings.findByName("Project");
        Assertions.assertNotNull(parent);
        Assertions.assertEquals(Project.class, parent.type());
    }

    @Test
    public void shouldFindByParentGroupByDiscriminatorValue() {
        Map<String, InheritanceMetadata> group = this.mappings
                .findByParentGroupByDiscriminatorValue(Notification.class);

        Assertions.assertEquals(4, group.size());
        Assertions.assertNotNull(group.get("SocialMediaNotification"));
        Assertions.assertNotNull(group.get("SMS"));
        Assertions.assertNotNull(group.get("Email"));
    }

    @Test
    public void shouldFindByParentGroupByDiscriminatorValue2() {
        Map<String, InheritanceMetadata> group = this.mappings
                .findByParentGroupByDiscriminatorValue(Project.class);

        Assertions.assertEquals(3, group.size());
        Assertions.assertNotNull(group.get("Small"));
        Assertions.assertNotNull(group.get("Large"));
        Assertions.assertNotNull(group.get("Project"));
    }

    @Test
    public void shouldLoadUsingGet(){
        this.mappings.load(Movie.class);
        EntityMetadata mapping = mappings.findByName("Movie");
        assertThat(mapping).isNotNull();
        Assertions.assertEquals(Movie.class, mapping.type());
    }
}