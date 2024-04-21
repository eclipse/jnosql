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

import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.eclipse.jnosql.communication.query.Where;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JakartaDataQueryProviderTest {



    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from entity"})
    void shouldReturnParserQuery(String query) {
        String entity = "entity";
        DeleteQuery deleteQuery = queryProvider.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.entity());
        assertTrue(deleteQuery.fields().isEmpty());
        Optional<Where> where = deleteQuery.where();
        assertFalse(where.isPresent());
    }

}
