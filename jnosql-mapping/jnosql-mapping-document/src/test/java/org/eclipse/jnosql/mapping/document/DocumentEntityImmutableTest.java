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
package org.eclipse.jnosql.mapping.document;

import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.document.entities.Car;
import org.eclipse.jnosql.mapping.document.entities.Hero;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@EnableAutoWeld
@AddPackages(value = {Convert.class, DocumentWorkflow.class})
@AddPackages(MockProducer.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
public class DocumentEntityImmutableTest {


    @Inject
    private DefaultDocumentEntityConverter converter;

    private Document[] columns;

    private Car car;

    @BeforeEach
    public void init() {

        this.car = new Car("123456789", "SF90", "Ferrari", Year.now());

        columns = new Document[]{Document.of("_id", "123456789"),
                Document.of("model", "SF90"),
                Document.of("manufacturer", "Ferrari"),
                Document.of("year", Year.now())
        };
    }

    @Test
    public void shouldConvertCommunicationEntity() {

        DocumentEntity entity = converter.toDocument(car);
        assertEquals("Car", entity.name());
        assertEquals(4, entity.size());
        assertThat(entity.documents()).contains(Document.of("_id", "123456789"),
                Document.of("model", "SF90"),
                Document.of("manufacturer", "Ferrari"));

    }

    @Test
    public void shouldConvertCommunicationEntity2() {

        DocumentEntity entity = converter.toDocument(car);
        assertEquals("Car", entity.name());
        assertEquals(4, entity.size());

        assertThat(entity.documents()).contains(columns);
    }

    @Test
    public void shouldConvertEntity() {
        DocumentEntity entity = DocumentEntity.of("Car");
        Stream.of(columns).forEach(entity::add);

        Car ferrari = converter.toEntity(Car.class, entity);
        assertNotNull(ferrari);
        assertEquals("123456789", ferrari.plate());
        assertEquals("SF90", ferrari.model());
        assertEquals("Ferrari", ferrari.manufacturer());
        assertEquals(Year.now(), ferrari.year());

    }

    @Test
    public void shouldConvertExistRecord() {
        DocumentEntity entity = DocumentEntity.of("Car");
        Stream.of(columns).forEach(entity::add);
        Car ferrari = new Car(null, null, null, null);
        Car result = converter.toEntity(ferrari, entity);

        assertEquals("123456789", result.plate());
        assertEquals("SF90", result.model());
        assertEquals("Ferrari", result.manufacturer());
        assertEquals(Year.now(), result.year());

        assertSoftly(soft -> {
            soft.assertThat(ferrari.model()).isNull();
            soft.assertThat(ferrari.manufacturer()).isNull();
            soft.assertThat(ferrari.plate()).isNull();
            soft.assertThat(ferrari.year()).isNull();

            soft.assertThat(result.model()).isEqualTo("SF90");
            soft.assertThat(result.manufacturer()).isEqualTo("Ferrari");
            soft.assertThat(result.plate()).isEqualTo("123456789");
            soft.assertThat(result.year()).isEqualTo(2023);
        });
    }

    @Test
    public void shouldConvertExist() {
        DocumentEntity entity = DocumentEntity.of("Hero");
        entity.add("_id", "2342");
        entity.add("name", "Iron man");
        Hero hero = new Hero(null, null);
        Hero result = converter.toEntity(hero, entity);
        assertSame(hero, result);
        assertSoftly(soft -> {
                    soft.assertThat(hero.id()).isEqualTo("2342");
                    soft.assertThat(hero.name()).isEqualTo("Iron man");
                }
        );
    }

}
