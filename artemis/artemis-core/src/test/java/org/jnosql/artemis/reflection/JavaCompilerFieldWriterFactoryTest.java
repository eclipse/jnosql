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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class JavaCompilerFieldWriterFactoryTest {

    private final JavaCompilerFacade compilerFacade = new JavaCompilerFacade(
            JavaCompilerBeanPropertyReaderFactory.class.getClassLoader());

    private final Reflections reflections = new DefaultReflections();

    private FieldWriterFactory fallback = new ReflectionFieldWriterFactory(reflections);

    private FieldWriterFactory factory = new JavaCompilerFieldWriterFactory(compilerFacade, reflections, fallback);


    @Test
    public void shouldCreateFieldWriter() {
        Foo foo = new Foo();
        foo.setBar("bar");
        FieldWriter writer = factory.apply(Foo.class.getDeclaredFields()[0]);
        Assertions.assertNotNull(writer);
        writer.write(foo, "bar");
        Assertions.assertEquals("bar", foo.getBar());
    }

    @Test
    public void shouldUseFallBackWhenThereIsNotSetter() throws IllegalAccessException {
        Foo foo = new Foo();
        Field field = Foo.class.getDeclaredFields()[1];
        field.setAccessible(true);
        FieldWriter writer = factory.apply(field);
        Assertions.assertNotNull(writer);
        writer.write(foo, "update");
        Assertions.assertEquals("update", field.get(foo));
    }

    @Test
    public void shouldUseFallBackWhenGetterIsNotPublic() throws IllegalAccessException {
        Foo foo = new Foo();
        Field field = Foo.class.getDeclaredFields()[2];
        field.setAccessible(true);

        FieldWriter writer = factory.apply(field);
        Assertions.assertNotNull(writer);
        writer.write(foo, "update");
        Assertions.assertEquals("update", field.get(foo));
    }
}