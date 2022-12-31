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


import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.eclipse.jnosql.mapping.reflection.MappingType.COLLECTION;
import static org.eclipse.jnosql.mapping.reflection.MappingType.DEFAULT;
import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.MappingType.MAP;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class FieldMappingTest {


    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldReadDefaultField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.getFields();

        FieldMapping field = fields.stream()
                .filter(f -> "string".equals(f.getFieldName())).findFirst().get();

        assertEquals("string", field.getFieldName());
        assertEquals("stringTypeAnnotation", field.getName());
        assertEquals(DEFAULT, field.getType());

    }

    @Test
    public void shouldReadCollectionField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.getFields();
        FieldMapping field = fields.stream()
                .filter(f -> "list".equals(f.getFieldName())).findFirst().get();

        assertEquals("list", field.getFieldName());
        assertEquals("listAnnotation", field.getName());
        assertEquals(COLLECTION, field.getType());
    }

    @Test
    public void shouldReadMapField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.getFields();
        FieldMapping field = fields.stream()
                .filter(f -> "map".equals(f.getFieldName())).findFirst().get();

        assertEquals("map", field.getFieldName());
        assertEquals("mapAnnotation", field.getName());
        assertEquals(MAP, field.getType());

    }

    @Test
    public void shouldReadEmbeddableField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.getFields();
        FieldMapping field = fields.stream()
                .filter(f -> "barClass".equals(f.getFieldName())).findFirst().get();

        assertEquals("barClass", field.getFieldName());
        assertEquals("barClass", field.getName());
        assertEquals(EMBEDDED, field.getType());
    }

    @Test
    public void shouldUserFieldReader() {
        ForClass forClass = new ForClass();
        forClass.string = "text";
        forClass.list = Collections.singletonList("text");
        forClass.map = Collections.singletonMap("key", "value");
        forClass.barClass = new BarClass();
        forClass.barClass.integer = 10;

        EntityMetadata entityMetadata = classConverter.create(ForClass.class);

        FieldMapping string = entityMetadata.getFieldMapping("string").get();
        FieldMapping list = entityMetadata.getFieldMapping("list").get();
        FieldMapping map = entityMetadata.getFieldMapping("map").get();
        FieldMapping barClass = entityMetadata.getFieldMapping("barClass").get();

        assertEquals("text", string.read(forClass));
        assertEquals(forClass.list, list.read(forClass));
        assertEquals(forClass.map, map.read(forClass));
        assertEquals(forClass.barClass, barClass.read(forClass));

    }

    @Test
    public void shouldUserFieldWriter() {
        ForClass forClass = new ForClass();
        BarClass value = new BarClass();
        value.integer = 10;

        EntityMetadata entityMetadata = classConverter.create(ForClass.class);

        FieldMapping string = entityMetadata.getFieldMapping("string").get();
        FieldMapping list = entityMetadata.getFieldMapping("list").get();
        FieldMapping map = entityMetadata.getFieldMapping("map").get();
        FieldMapping barClass = entityMetadata.getFieldMapping("barClass").get();

        string.write(forClass, "text");
        list.write(forClass, Collections.singletonList("text"));
        map.write(forClass, Collections.singletonMap("key", "value"));
        barClass.write(forClass, value);

        assertEquals("text", string.read(forClass));
        assertEquals(forClass.list, list.read(forClass));
        assertEquals(forClass.map, map.read(forClass));
        assertEquals(forClass.barClass, barClass.read(forClass));
    }


    public static class ForClass {

        @Column("stringTypeAnnotation")
        private String string;

        @Column("listAnnotation")
        private List<String> list;

        @Column("mapAnnotation")
        private Map<String, String> map;


        @Column
        private BarClass barClass;
    }

    @Embeddable
    public static class BarClass {

        @Column("integerAnnotation")
        private Integer integer;
    }

}
