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
package org.eclipse.jnosql.mapping.column;

import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.column.ColumnObserverParser;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.column.entities.Car;
import org.eclipse.jnosql.mapping.column.entities.Vendor;
import org.eclipse.jnosql.mapping.column.entities.Worker;
import org.eclipse.jnosql.mapping.column.spi.ColumnExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@EnableAutoWeld
@AddPackages(value = {Converters.class, ColumnEntityConverter.class})
@AddPackages(MockProducer.class)
@AddExtensions({EntityMetadataExtension.class, ColumnExtension.class})
class ColumnMapperObserverTest {

    @Inject
    private EntitiesMetadata mappings;

    private ColumnObserverParser parser;

    @BeforeEach
    public void setUp() {
        this.parser = new ColumnMapperObserver(mappings);
    }

    @Test
    public void shouldFireEntity(){
        var entity = parser.fireEntity("Vendor");
        Assertions.assertEquals("vendors", entity);
    }

    @Test
    public void shouldFireFromClass(){
        var entity = parser.fireEntity(Car.class.getSimpleName());
        Assertions.assertEquals("Car", entity);
    }

    @Test
    public void shouldFireFromClassName(){
        var entity = parser.fireEntity(Car.class.getSimpleName());
        Assertions.assertEquals("Car", entity);
    }

    @Test
    public void shouldFireField(){
        var field = parser.fireField("Worker", "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    public void shouldFireFieldFromClassName(){
        var field = parser.fireField(Worker.class.getName(), "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    public void shouldFireFieldFromSimplesName(){
        var field = parser.fireField(Worker.class.getSimpleName(), "salary");
        Assertions.assertEquals("money", field);
    }

    @Test
    public void shouldFireFieldFromEntity(){
        var field = parser.fireField(Vendor.class.getSimpleName(), "name");
        Assertions.assertEquals("_id", field);
    }

}