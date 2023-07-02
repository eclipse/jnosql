/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *
 * Maximillian Arruda
 *
 */
package org.eclipse.jnosql.communication.column;

import org.eclipse.jnosql.communication.Params;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ColumnDeleteQueryParamsTest {

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldInstantiateSuccessfully(ColumnDeleteQuery query, Params params) {
        ColumnDeleteQueryParams target = newInstance(query, params);
        assertThat(target).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldReturnTheSameQueryInstance(ColumnDeleteQuery expectedQuery, Params params) {
        var target = newInstance(expectedQuery, params);
        assertThat(expectedQuery).isSameAs(target.query());
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldReturnTheSameParamsInstance(ColumnDeleteQuery query, Params expectedParams) {
        var target = newInstance(query, expectedParams);
        assertThat(expectedParams).isSameAs(target.params());
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldBeNotEqualsToNull(ColumnDeleteQuery query, Params params) {
        var instance = newInstance(query, params);
        assertThat(instance).isNotEqualTo(null);
    }


    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldBeNotEqualsToAnyOtherInstanceOfDifferentType(ColumnDeleteQuery query, Params params) {
        var instance = newInstance(query, params);
        assertThat(instance).isNotEqualTo(new Object());
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldBeEqualsToItself(ColumnDeleteQuery query, Params params) {
        var instance = newInstance(query, params);
        assertThat(instance).isEqualTo(instance);
    }

    @Test
    void shouldBeEqualsWhenQueryAndParamsAreUsedByTwoDifferentInstances() {
        var query = newDummyColumnDeleteQuery();
        var params = newDummyParams();

        var leftInstance = newInstance(query, params);
        var rightInstance = newInstance(query, params);

        assertThat(leftInstance).isEqualTo(rightInstance);
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldBeNotEqualsWhenDifferentQueryAndParamsAreUsedByTwoDifferentInstances(ColumnDeleteQuery query, Params params) {

        var leftInstance = newInstance(query, params);
        var rightInstance = newInstance(newDummyColumnDeleteQuery(), newDummyParams());

        assertThat(leftInstance).isNotEqualTo(rightInstance);
    }

    @Test
    void shouldHashCodeBeConditionedToQueryAndParamsAttributes() {

        ColumnDeleteQuery firstQuery = newDummyColumnDeleteQuery();
        Params firstParams = newDummyParams();

        var fistInstance = newInstance(firstQuery, firstParams);
        var secondInstance = newInstance(firstQuery, firstParams);

        assertThat(fistInstance.hashCode()).isEqualTo(secondInstance.hashCode());

        ColumnDeleteQuery secondQuery = newDummyColumnDeleteQuery();
        Params secondParams = newDummyParams();

        var thirdInstance = newInstance(secondQuery, secondParams);

        assertThat(fistInstance.hashCode()).isNotEqualTo(thirdInstance.hashCode());

    }

    static Stream<Arguments> scenarios() {
        return Stream.of(
                givenNullArguments(),
                givenColumnDeleteQueryOnly(),
                givenParamsOnly(),
                givenValidArguments()
        );
    }

    private ColumnDeleteQueryParams newInstance(ColumnDeleteQuery query, Params params) {
        return new ColumnDeleteQueryParams(query,params);
    }

    private static Params newDummyParams() {
        Params params = Params.newParams();
        params.add(UUID.randomUUID().toString());
        return params;
    }

    private static ColumnDeleteQuery newDummyColumnDeleteQuery() {
        return ColumnDeleteQuery.builder().from(UUID.randomUUID().toString()).build();
    }

    private static Arguments givenValidArguments() {
        return arguments(newDummyColumnDeleteQuery(), newDummyParams());
    }

    private static Arguments givenParamsOnly() {
        return arguments(null, newDummyParams());
    }

    private static Arguments givenColumnDeleteQueryOnly() {
        return arguments(newDummyColumnDeleteQuery(), null);
    }

    private static Arguments givenNullArguments() {
        return arguments(null, null);
    }

}
