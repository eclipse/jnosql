/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.data;

import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JakartaDataQueryProviderTest {


    private SelectJDQL selectProvider;

    @BeforeEach
    void setUp() {
        selectProvider = new SelectJDQL();
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"FROM entity"})
    void shouldReturnParserQuery2(String query) {
        SelectQuery selectQuery = selectProvider.apply(query, "entity");

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(selectQuery.fields()).isEmpty();
            soft.assertThat(selectQuery.orderBy()).isEmpty();
            soft.assertThat(selectQuery.where()).isEmpty();
            soft.assertThat(selectQuery.entity()).isEqualTo("entity");
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"FROM entity"})
    void shouldOverwriteTheEntity(String query) {
        SelectQuery selectQuery = selectProvider.apply(query, "newEntity");

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(selectQuery.fields()).isEmpty();
            soft.assertThat(selectQuery.orderBy()).isEmpty();
            soft.assertThat(selectQuery.where()).isEmpty();
            soft.assertThat(selectQuery.entity()).isEqualTo("entity");
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"", " "})
    void shouldKeepEntityFromParameter(String query) {
        SelectQuery selectQuery = selectProvider.apply(query, "entity");

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(selectQuery.fields()).isEmpty();
            soft.assertThat(selectQuery.orderBy()).isEmpty();
            soft.assertThat(selectQuery.where()).isEmpty();
            soft.assertThat(selectQuery.entity()).isEqualTo("entity");
        });
    }



}
