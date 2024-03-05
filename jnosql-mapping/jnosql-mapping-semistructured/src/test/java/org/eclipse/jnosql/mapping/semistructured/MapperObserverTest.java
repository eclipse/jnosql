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
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.semistructured.CommunicationObserverParser;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.semistructured.entities.Car;
import org.eclipse.jnosql.mapping.semistructured.entities.Vendor;
import org.eclipse.jnosql.mapping.semistructured.entities.Worker;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class})
class MapperObserverTest {

    @Inject
    private EntitiesMetadata mappings;

    private CommunicationObserverParser parser;

    @BeforeEach
    void setUp() {
        this.parser = new MapperObserver(mappings);
    }

    @Test
    void shouldFireEntity(){
        var entity = parser.fireEntity("Vendor");
        Assertions.assertEquals("vendors", entity);
    }

    @Test
    void shouldFireFromClass(){
        var entity = parser.fireEntity(Car.class.getSimpleName());
        Assertions.assertEquals("Car", entity);
    }

    @Test
    void shouldFireFromClassName(){
        var entity = parser.fireEntity(Car.class.getSimpleName());
        Assertions.assertEquals("Car", entity);
    }

    @Test
    void shouldFireField(){
        var field = parser.fireField("Worker", "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    void shouldFireFieldFromClassName(){
        var field = parser.fireField(Worker.class.getName(), "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    void shouldFireFieldFromSimplesName(){
        var field = parser.fireField(Worker.class.getSimpleName(), "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    void shouldFireFieldFromEntity(){
        var field = parser.fireField(Vendor.class.getSimpleName(), "name");
        Assertions.assertEquals("_id", field);
    }

}