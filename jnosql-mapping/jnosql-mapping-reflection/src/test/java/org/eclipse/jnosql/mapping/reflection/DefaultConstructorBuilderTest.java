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
package org.eclipse.jnosql.mapping.reflection;

import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.ReflectionGroupEntityMetadata;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.test.entities.constructor.BookUser;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = ReflectionGroupEntityMetadata.class)
class DefaultConstructorBuilderTest {

    private ConstructorMetadata constructor;

    @BeforeEach
    public void setUp(){
        ClassConverter converter = new ReflectionClassConverter();
        EntityMetadata entityMetadata = converter.apply(BookUser.class);
        this.constructor = entityMetadata.constructor();
    }

    @Test
    public void shouldToString(){
        ConstructorBuilder builder = DefaultConstructorBuilder.of(constructor);
        assertThat(builder.toString()).isNotEmpty().isNotBlank().isNotNull();
    }

    @Test
    public void shouldCreateEmpty(){
        ConstructorBuilder builder = DefaultConstructorBuilder.of(constructor);
        builder.addEmptyParameter();
        builder.addEmptyParameter();
        builder.addEmptyParameter();
        BookUser user = builder.build();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(user.getName()).isNull();
            soft.assertThat(user.getNickname()).isNull();
            soft.assertThat(user.getBooks()).isNull();
        });
    }


    @Test
    public void shouldCreateWithValues(){
        ConstructorBuilder builder = DefaultConstructorBuilder.of(constructor);
        builder.add("id");
        builder.add("name");
        builder.addEmptyParameter();

        BookUser user = builder.build();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(user.getNickname()).isEqualTo("id");
            soft.assertThat(user.getName()).isEqualTo("name");
            soft.assertThat(user.getBooks()).isNull();
        });
    }

    @Test
    public void shouldParameters(){
        ConstructorBuilder builder = DefaultConstructorBuilder.of(constructor);
        builder.add("id");
        builder.add("name");
        builder.addEmptyParameter();

        assertThat(builder.parameters()).hasSize(3);
    }

    @Test
    public void shouldEqualsHashCode(){
        ConstructorBuilder builder = DefaultConstructorBuilder.of(constructor);
        ConstructorBuilder other = DefaultConstructorBuilder.of(constructor);
        assertThat(builder).isEqualTo(other);
        assertThat(builder).hasSameHashCodeAs(other);
    }
}
