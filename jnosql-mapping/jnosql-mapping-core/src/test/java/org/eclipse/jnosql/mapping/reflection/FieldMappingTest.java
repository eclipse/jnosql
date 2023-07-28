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


import jakarta.nosql.Column;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
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

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)
@AddExtensions(EntityMetadataExtension.class)
public class FieldMappingTest {


    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldReadDefaultField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.fields();

        FieldMapping field = fields.stream()
                .filter(f -> "string".equals(f.fieldName())).findFirst().get();

        assertEquals("string", field.fieldName());
        assertEquals("stringTypeAnnotation", field.name());
        assertEquals(DEFAULT, field.type());

    }

    @Test
    public void shouldReadCollectionField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.fields();
        FieldMapping field = fields.stream()
                .filter(f -> "list".equals(f.fieldName())).findFirst().get();

        assertEquals("list", field.fieldName());
        assertEquals("listAnnotation", field.name());
        assertEquals(COLLECTION, field.type());
    }

    @Test
    public void shouldReadMapField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.fields();
        FieldMapping field = fields.stream()
                .filter(f -> "map".equals(f.fieldName())).findFirst().get();

        assertEquals("map", field.fieldName());
        assertEquals("mapAnnotation", field.name());
        assertEquals(MAP, field.type());

    }

    @Test
    public void shouldReadEmbeddableField() {
        EntityMetadata entityMetadata = classConverter.create(ForClass.class);
        List<FieldMapping> fields = entityMetadata.fields();
        FieldMapping field = fields.stream()
                .filter(f -> "barClass".equals(f.fieldName())).findFirst().get();

        assertEquals("barClass", field.fieldName());
        assertEquals("barClass", field.name());
        assertEquals(EMBEDDED, field.type());
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

        FieldMapping string = entityMetadata.fieldMapping("string").get();
        FieldMapping list = entityMetadata.fieldMapping("list").get();
        FieldMapping map = entityMetadata.fieldMapping("map").get();
        FieldMapping barClass = entityMetadata.fieldMapping("barClass").get();

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

        FieldMapping string = entityMetadata.fieldMapping("string").get();
        FieldMapping list = entityMetadata.fieldMapping("list").get();
        FieldMapping map = entityMetadata.fieldMapping("map").get();
        FieldMapping barClass = entityMetadata.fieldMapping("barClass").get();

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

    public static class BarClass {

        @Column("integerAnnotation")
        private Integer integer;
    }

}
