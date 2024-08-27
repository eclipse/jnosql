/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class QueryTypeTest {


    @Test
    void shouldParseSelectQuery() {
        String query = "SELECT * FROM table";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.SELECT);
    }

    @Test
    void shouldParseDeleteQuery() {
        String query = "DELETE FROM table WHERE id = 1";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.DELETE);
    }

    @Test
    void shouldParseUpdateQuery() {
        String query = "UPDATE table SET name = 'newName' WHERE id = 1";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.UPDATE);
    }

    @Test
    void shouldDefaultToSelectForUnknownQuery() {
        String query = "INSERT INTO table (id, name) VALUES (1, 'name')";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.SELECT);
    }

    @Test
    void shouldDefaultToSelectForShortQuery() {
        String query = "DELE";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.SELECT);
    }

    @Test
    void shouldDefaultToSelectForEmptyQuery() {
        String query = "";
        QueryType result = QueryType.parse(query);
        assertThat(result).isEqualTo(QueryType.SELECT);
    }

    @Test
    void shouldThrowNullPointerExceptionForNullQuery() {
        String query = null;
        assertThatThrownBy(() -> QueryType.parse(query))
                .isInstanceOf(NullPointerException.class);
    }
}