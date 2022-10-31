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

class MethodQueryTest {

    @Test
    public void shouldReturnNullWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> MethodQuery.of(null));
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
    @ValueSource(strings = {"findByLastNameAndFirstName"})
    public void shouldRunQuery6(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy LastName And FirstName", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByLastNameOrFirstName"})
    public void shouldRunQuery7(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy LastName Or FirstName", methodQuery.get());
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
    @ValueSource(strings = {"findByFirstNameLike"})
    public void shouldRunQuery13(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy FirstName Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameNotLike"})
    public void shouldRunQuery14(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy FirstName Not  Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameLikeOrderByNameAscAgeDesc"})
    public void shouldRunQuery15(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy FirstName Like  OrderBy Name Asc Age Desc", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByFirstNameLikeOrderByNameAscAge"})
    public void shouldRunQuery16(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy FirstName Like  OrderBy Name Asc Age", methodQuery.get());
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
    @ValueSource(strings = {"deleteByLastNameAndFirstName"})
    public void shouldRunQuery20(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy LastName And FirstName", methodQuery.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByLastNameOrFirstName"})
    public void shouldRunQuery21(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy LastName Or FirstName", methodQuery.get());
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
    @ValueSource(strings = {"deleteByFirstNameLike"})
    public void shouldRunQuery27(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy FirstName Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByFirstNameNotLike"})
    public void shouldRunQuery28(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy FirstName Not  Like", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_Currency"})
    public void shouldRunQuery29(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Salary_Currency", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_CurrencyAndCredential_Role"})
    public void shouldRunQuery30(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Salary_Currency And Credential_Role", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBySalary_CurrencyAndName"})
    public void shouldRunQuery31(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("deleteBy Salary_Currency And Name", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_Currency"})
    public void shouldRunQuery32(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Salary_Currency", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndCredential_Role"})
    public void shouldRunQuery33(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Salary_Currency And Credential_Role", methodQuery.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndName"})
    public void shouldRunQuery34(String query) {
        MethodQuery methodQuery = MethodQuery.of(query);
        assertNotNull(methodQuery);
        assertEquals("findBy Salary_Currency And Name", methodQuery.get());
    }


}