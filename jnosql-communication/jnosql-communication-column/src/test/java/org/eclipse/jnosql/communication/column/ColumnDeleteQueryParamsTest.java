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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnDeleteQueryParamsTest {

    private static Params newDummyParams() {
        Params params = Params.newParams();
        params.add(UUID.randomUUID().toString());
        return params;
    }

    private static ColumnDeleteQuery newDummyColumnDeleteQuery() {
        return ColumnDeleteQuery.builder().from(UUID.randomUUID().toString()).build();
    }

    @Test
    void shouldInstantiateWithNullArguments() {
        new ColumnDeleteQueryParams(null, null);
    }

    @Test
    void shouldInstantiateWithParamsOnly() {
        new ColumnDeleteQueryParams(null, newDummyParams());
    }

    @Test
    void shouldInstantiateWithQueryOnly() {
        new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), null);
    }

    @Test
    void shouldInstantiateWithNonNullArgs() {
        new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());
    }


    @Test
    void shouldReturnTheSameQueryInstance() {
        var expectedQuery = newDummyColumnDeleteQuery();
        var target = new ColumnDeleteQueryParams(expectedQuery, Params.newParams());
        assertThat(expectedQuery).isSameAs(target.query());
    }

    @Test
    void shouldReturnTheSameParamsInstance() {
        var expectedParams = Params.newParams();
        var target = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), expectedParams);
        assertThat(expectedParams).isSameAs(target.params());
    }

    @Test
    void shouldBeNotEqualsToNull() {

        var instance = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());

        assertThat(instance).isNotEqualTo(null);
    }

    @Test
    void shouldBeNotEqualsToAnyOtherInstanceOfDifferentType() {

        var instance = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());

        assertThat(instance).isNotEqualTo(new Object());
    }

    @Test
    void shouldBeEqualsToItself() {
        var instance = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());

        assertThat(instance).isEqualTo(instance);
    }

    @Test
    void shouldBeEqualsWhenQueryAndParamsAreUsedByTwoDifferentInstances() {
        var query = newDummyColumnDeleteQuery();
        var params = newDummyParams();

        var leftInstance = new ColumnDeleteQueryParams(query, params);
        var rightInstance = new ColumnDeleteQueryParams(query, params);

        assertThat(leftInstance).isEqualTo(rightInstance);
    }

    @Test
    void shouldBeNotEqualsWhenDifferentQueryAndParamsAreUsedByTwoDifferentInstances() {

        var leftInstance = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());
        var rightInstance = new ColumnDeleteQueryParams(newDummyColumnDeleteQuery(), newDummyParams());

        assertThat(leftInstance).isNotEqualTo(rightInstance);
    }

    @Test
    void shouldHashCodeBeConditionedToQueryAndParamsAttributes() {

        ColumnDeleteQuery firstQuery = newDummyColumnDeleteQuery();
        Params firstParams = newDummyParams();

        var fistInstance = new ColumnDeleteQueryParams(firstQuery, firstParams);
        var secondInstance = new ColumnDeleteQueryParams(firstQuery, firstParams);

        assertThat(fistInstance.hashCode()).isEqualTo(secondInstance.hashCode());

        ColumnDeleteQuery secondQuery = newDummyColumnDeleteQuery();
        Params secondParams = newDummyParams();

        var thirdInstance = new ColumnDeleteQueryParams(secondQuery, secondParams);

        assertThat(fistInstance.hashCode()).isNotEqualTo(thirdInstance.hashCode());

    }
}