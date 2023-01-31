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
package org.eclipse.jnosql.mapping.column.query;

import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.communication.column.ColumnQueryParams;
import org.eclipse.jnosql.communication.column.SelectQueryParser;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.column.ColumnWorkflow;
import org.eclipse.jnosql.mapping.column.MockProducer;
import org.eclipse.jnosql.mapping.column.spi.ColumnExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.column.entities.Person;

import org.eclipse.jnosql.mapping.util.ParamsBinder;
import org.eclipse.jnosql.communication.query.method.SelectMethodProvider;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoWeld
@AddPackages(value = {Convert.class, ColumnWorkflow.class})
@AddPackages(MockProducer.class)
@AddExtensions({EntityMetadataExtension.class, ColumnExtension.class})
class ParamsBinderTest {


    @Inject
    private EntitiesMetadata mappings;

    @Inject
    private Converters converters;

    private ParamsBinder paramsBinder;

    @Test
    public void shouldConvert() {

        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals("findByAge")).findFirst().get();
        EntityMetadata entityMetadata = mappings.get(Person.class);
        RepositoryColumnObserverParser parser = new RepositoryColumnObserverParser(entityMetadata);
        paramsBinder = new ParamsBinder(entityMetadata, converters);

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.INSTANCE;
        SelectQuery selectQuery = selectMethodFactory.apply(method, entityMetadata.getName());
        SelectQueryParser queryParser = new SelectQueryParser();
        ColumnQueryParams columnQueryParams = queryParser.apply(selectQuery, parser);
        Params params = columnQueryParams.params();
        Object[] args = {10};
        paramsBinder.bind(params, args, method);
        ColumnQuery query = columnQueryParams.query();
        ColumnCondition columnCondition = query.condition().get();
        Value value = columnCondition.column().value();
        assertEquals(10, value.get());

    }

    @Test
    public void shouldConvert2() {

        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals("findByAgeAndName")).findFirst().get();
        EntityMetadata entityMetadata = mappings.get(Person.class);
        RepositoryColumnObserverParser parser = new RepositoryColumnObserverParser(entityMetadata);
        paramsBinder = new ParamsBinder(entityMetadata, converters);

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.INSTANCE;
        SelectQuery selectQuery = selectMethodFactory.apply(method, entityMetadata.getName());
        SelectQueryParser queryParser = new SelectQueryParser();
        ColumnQueryParams queryParams = queryParser.apply(selectQuery, parser);
        Params params = queryParams.params();
        paramsBinder.bind(params, new Object[]{10L, "Ada"}, method);
        ColumnQuery query = queryParams.query();
        ColumnCondition columnCondition = query.condition().get();
        List<ColumnCondition> conditions = columnCondition.column().get(new TypeReference<>() {
        });
        List<Object> values = conditions.stream().map(ColumnCondition::column)
                .map(Column::value)
                .map(Value::get).collect(Collectors.toList());
        assertEquals(10, values.get(0));
        assertEquals("Ada", values.get(1));

    }


    interface PersonRepository {

        List<Person> findByAge(Integer age);

        List<Person> findByAgeAndName(Long age, String name);
    }


}