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
package org.jnosql.artemis.document.query;


import org.jnosql.aphrodite.antlr.method.SelectMethodFactory;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.artemis.util.ParamsBinder;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.document.query.DocumentQueryParams;
import org.jnosql.diana.api.document.query.SelectQueryConverter;
import org.jnosql.query.Params;
import org.jnosql.query.SelectQuery;
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
        RepositoryDocumentObserverParser parser = new RepositoryDocumentObserverParser(classMapping);
        paramsBinder = new ParamsBinder(classMapping, converters);

        SelectMethodFactory selectMethodFactory = SelectMethodFactory.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, classMapping.getName());
        SelectQueryConverter converter = SelectQueryConverter.get();
        DocumentQueryParams columnQueryParams = converter.apply(selectQuery, parser);
        Params params = columnQueryParams.getParams();
        Object[] args = {10};
        paramsBinder.bind(params, args, method);
        DocumentQuery query = columnQueryParams.getQuery();
        DocumentCondition columnCondition = query.getCondition().get();
        Value value = columnCondition.getDocument().getValue();
        assertEquals(10, value.get());

    }

    @Test
    public void shouldConvert2() {

        Method method = Stream.of(PersonRepository.class.getMethods())
                .filter(m -> m.getName().equals("findByAgeAndName")).findFirst().get();
        ClassMapping classMapping = mappings.get(Person.class);
        RepositoryDocumentObserverParser parser = new RepositoryDocumentObserverParser(classMapping);
        paramsBinder = new ParamsBinder(classMapping, converters);

        SelectMethodFactory selectMethodFactory = SelectMethodFactory.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, classMapping.getName());
        SelectQueryConverter converter = SelectQueryConverter.get();
        DocumentQueryParams queryParams = converter.apply(selectQuery, parser);
        Params params = queryParams.getParams();
        paramsBinder.bind(params, new Object[]{10L, "Ada"}, method);
        DocumentQuery query = queryParams.getQuery();
        DocumentCondition columnCondition = query.getCondition().get();
        List<DocumentCondition> conditions = columnCondition.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        List<Object> values = conditions.stream().map(DocumentCondition::getDocument)
                .map(Document::getValue)
                .map(Value::get).collect(Collectors.toList());
        assertEquals(10, values.get(0));
        assertEquals("Ada", values.get(1));

    }


    interface PersonRepository {

        List<Person> findByAge(Integer age);

        List<Person> findByAgeAndName(Long age, String name);
    }


}