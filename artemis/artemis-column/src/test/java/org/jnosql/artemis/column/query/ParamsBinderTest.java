/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParams;
import jakarta.nosql.column.SelectQueryConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.query.SelectQuery;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.util.ParamsBinder;
import org.eclipse.jnosql.diana.query.method.SelectMethodProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(CDIExtension.class)
class ParamsBinderTest {


    @Inject
    private ClassMappings mappings;

    @Inject
    private Converters converters;

    private ParamsBinder paramsBinder;

    @Test
    public void shouldConvert() {

        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals("findByAge")).findFirst().get();
        ClassMapping classMapping = mappings.get(Person.class);
        RepositoryColumnObserverParser parser = new RepositoryColumnObserverParser(classMapping);
        paramsBinder = new ParamsBinder(classMapping, converters);

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, classMapping.getName());
        SelectQueryConverter converter = ServiceLoaderProvider.get(SelectQueryConverter.class);
        ColumnQueryParams columnQueryParams = converter.apply(selectQuery, parser);
        Params params = columnQueryParams.getParams();
        Object[] args = {10};
        paramsBinder.bind(params, args, method);
        ColumnQuery query = columnQueryParams.getQuery();
        ColumnCondition columnCondition = query.getCondition().get();
        Value value = columnCondition.getColumn().getValue();
        assertEquals(10, value.get());

    }

    @Test
    public void shouldConvert2() {

        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals("findByAgeAndName")).findFirst().get();
        ClassMapping classMapping = mappings.get(Person.class);
        RepositoryColumnObserverParser parser = new RepositoryColumnObserverParser(classMapping);
        paramsBinder = new ParamsBinder(classMapping, converters);

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, classMapping.getName());
        SelectQueryConverter converter = ServiceLoaderProvider.get(SelectQueryConverter.class);
        ColumnQueryParams queryParams = converter.apply(selectQuery, parser);
        Params params = queryParams.getParams();
        paramsBinder.bind(params, new Object[]{10L, "Ada"}, method);
        ColumnQuery query = queryParams.getQuery();
        ColumnCondition columnCondition = query.getCondition().get();
        List<ColumnCondition> conditions = columnCondition.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        List<Object> values = conditions.stream().map(ColumnCondition::getColumn)
                .map(Column::getValue)
                .map(Value::get).collect(Collectors.toList());
        assertEquals(10, values.get(0));
        assertEquals("Ada", values.get(1));

    }


    interface PersonRepository {

        List<Person> findByAge(Integer age);

        List<Person> findByAgeAndName(Long age, String name);
    }


}