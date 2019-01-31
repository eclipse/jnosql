/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr.method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MethodQueryTest {

    @Test
    public void shouldReturnNullWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->{
           MethodQuery.of(null);
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAge"})
    public void shouldRunQuery(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameAndAge"})
    public void shouldRunQuery1(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Name And Age", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAge"})
    public void shouldRunQuery2(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Name Or Age", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByName"})
    public void shouldRunQuery3(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Name Or Age OrderBy Name", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByNameAsc"})
    public void shouldRunQuery4(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Name Or Age OrderBy Name Asc", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByNameDesc"})
    public void shouldRunQuery5(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Name Or Age OrderBy Name Desc", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByLastnameAndFirstname"})
    public void shouldRunQuery6(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Lastname And Firstname", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByLastnameOrFirstname"})
    public void shouldRunQuery7(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Lastname Or Firstname", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByStartDateBetween"})
    public void shouldRunQuery8(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy StartDate Between", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThan"})
    public void shouldRunQuery9(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age LessThan", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    public void shouldRunQuery10(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age LessThanEqual", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThan"})
    public void shouldRunQuery11(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age GreaterThan", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanEqual"})
    public void shouldRunQuery12(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age GreaterThanEqual", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstnameLike"})
    public void shouldRunQuery13(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Firstname Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstnameNotLike"})
    public void shouldRunQuery14(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Firstname Not  Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstnameLikeOrderByNameAscAgeDesc"})
    public void shouldRunQuery15(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Firstname Like  OrderBy Name Asc Age Desc", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstnameLikeOrderByNameAscAge"})
    public void shouldRunQuery16(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Firstname Like  OrderBy Name Asc Age", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAge"})
    public void shouldRunQuery17(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Age", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameAndAge"})
    public void shouldRunQuery18(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Name And Age", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameOrAge"})
    public void shouldRunQuery19(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Name Or Age", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByLastnameAndFirstname"})
    public void shouldRunQuery20(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Lastname And Firstname", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByLastnameOrFirstname"})
    public void shouldRunQuery21(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Lastname Or Firstname", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByStartDateBetween"})
    public void shouldRunQuery22(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy StartDate Between", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThan"})
    public void shouldRunQuery23(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Age LessThan", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    public void shouldRunQuery24(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Age LessThanEqual", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThan"})
    public void shouldRunQuery25(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Age GreaterThan", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThanEqual"})
    public void shouldRunQuery26(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Age GreaterThanEqual", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByFirstnameLike"})
    public void shouldRunQuery27(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Firstname Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByFirstnameNotLike"})
    public void shouldRunQuery28(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Firstname Not  Like", methodQuery.get());
    }

}