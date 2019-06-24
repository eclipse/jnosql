/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.jnosql.artemis.reflection;

import jakarta.nosql.mapping.reflection.FieldReader;
import jakarta.nosql.mapping.reflection.FieldReaderFactory;
import jakarta.nosql.mapping.reflection.Reflections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class JavaCompilerFieldReaderFactoryTest {

    private final JavaCompilerFacade compilerFacade = new JavaCompilerFacade(
            JavaCompilerBeanPropertyReaderFactory.class.getClassLoader());

    private final Reflections reflections = new DefaultReflections();

    private FieldReaderFactory fallback = new ReflectionFieldReaderFactory(reflections);

    private FieldReaderFactory factory = new JavaCompilerFieldReaderFactory(compilerFacade, reflections, fallback);


    @Test
    public void shouldCreateFieldReader() {
        Foo foo = new Foo();
        foo.setBar("bar");
        FieldReader fieldReader = factory.apply(Foo.class.getDeclaredFields()[0]);
        Assertions.assertNotNull(fieldReader);
        Object value = fieldReader.read(foo);
        Assertions.assertEquals("bar", value);
    }

    @Test
    public void shouldUseFallBackWhenThereIsNotGetter() {
        Foo foo = new Foo();
        Field field = Foo.class.getDeclaredFields()[1];
        field.setAccessible(true);
        FieldReader fieldReader = factory.apply(field);
        Assertions.assertNotNull(fieldReader);
        Object value = fieldReader.read(foo);
        Assertions.assertEquals("bar2", value);
    }

    @Test
    public void shouldUseFallBackWhenGetterIsNotPublic() {
        Foo foo = new Foo();
        Field field = Foo.class.getDeclaredFields()[2];
        field.setAccessible(true);

        FieldReader fieldReader = factory.apply(field);
        Assertions.assertNotNull(fieldReader);
        Object value = fieldReader.read(foo);
        Assertions.assertEquals("bar3", value);
    }

}