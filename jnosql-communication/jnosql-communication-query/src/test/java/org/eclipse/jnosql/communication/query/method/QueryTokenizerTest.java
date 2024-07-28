/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QueryTokenizerTest {

    @Test
    void shouldReturnNullWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> QueryTokenizer.of(null));
    }

    @Test
    void shouldEquals() {
        QueryTokenizer query = QueryTokenizer.of("findByAge");
        Assertions.assertEquals(query, QueryTokenizer.of("findByAge"));
    }

    @Test
    void shouldHashCode() {
        QueryTokenizer query = QueryTokenizer.of("findByAge");
        Assertions.assertEquals(query.hashCode(), QueryTokenizer.of("findByAge").hashCode());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAge"})
    void shouldRunQuery(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameAndAge"})
    void shouldRunQuery1(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Name And Age", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAge"})
    void shouldRunQuery2(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Name Or Age", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByName"})
    void shouldRunQuery3(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Name Or Age OrderBy Name", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByNameAsc"})
    void shouldRunQuery4(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Name Or Age OrderBy Name Asc", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameOrAgeOrderByNameDesc"})
    void shouldRunQuery5(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Name Or Age OrderBy Name Desc", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByLastNameAndFirstName"})
    void shouldRunQuery6(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy LastName And FirstName", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByLastNameOrFirstName"})
    void shouldRunQuery7(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy LastName Or FirstName", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByStartDateBetween"})
    void shouldRunQuery8(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy StartDate Between", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThan"})
    void shouldRunQuery9(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age LessThan", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    void shouldRunQuery10(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age LessThanEqual", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThan"})
    void shouldRunQuery11(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age GreaterThan", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanEqual"})
    void shouldRunQuery12(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age GreaterThanEqual", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameLike"})
    void shouldRunQuery13(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy FirstName Like", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameNotLike"})
    void shouldRunQuery14(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy FirstName Not Like", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameLikeOrderByNameAscAgeDesc"})
    void shouldRunQuery15(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy FirstName Like OrderBy Name Asc Age Desc", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameLikeOrderByNameAscAge"})
    void shouldRunQuery16(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy FirstName Like OrderBy Name Asc Age", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAge"})
    void shouldRunQuery17(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Age", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameAndAge"})
    void shouldRunQuery18(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Name And Age", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameOrAge"})
    void shouldRunQuery19(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Name Or Age", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByLastNameAndFirstName"})
    void shouldRunQuery20(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy LastName And FirstName", queryTokenizer.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByLastNameOrFirstName"})
    void shouldRunQuery21(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy LastName Or FirstName", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByStartDateBetween"})
    void shouldRunQuery22(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy StartDate Between", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThan"})
    void shouldRunQuery23(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Age LessThan", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    void shouldRunQuery24(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Age LessThanEqual", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThan"})
    void shouldRunQuery25(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Age GreaterThan", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThanEqual"})
    void shouldRunQuery26(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Age GreaterThanEqual", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByFirstNameLike"})
    void shouldRunQuery27(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy FirstName Like", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByFirstNameNotLike"})
    void shouldRunQuery28(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy FirstName Not Like", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_Currency"})
    void shouldRunQuery29(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Salary_Currency", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_CurrencyAndCredential_Role"})
    void shouldRunQuery30(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Salary_Currency And Credential_Role", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_CurrencyAndName"})
    void shouldRunQuery31(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("deleteBy Salary_Currency And Name", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_Currency"})
    void shouldRunQuery32(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Salary_Currency", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndCredential_Role"})
    void shouldRunQuery33(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Salary_Currency And Credential_Role", queryTokenizer.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndName"})
    void shouldRunQuery34(String query) {
        QueryTokenizer queryTokenizer = QueryTokenizer.of(query);
        assertNotNull(queryTokenizer);
        assertEquals("findBy Salary_Currency And Name", queryTokenizer.get());
    }


}