/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.eclipse.jnosql.communication.query.BooleanQueryValue.FALSE;
import static org.eclipse.jnosql.communication.query.BooleanQueryValue.TRUE;
import static org.junit.jupiter.api.Assertions.*;

class BooleanQueryValueTest {

    @Test
    public void shouldReturnType() {
        BooleanQueryValue value = TRUE;
        Assertions.assertThat(value).isNotNull().extracting(BooleanQueryValue::type)
                .isEqualTo(ValueType.BOOLEAN);
    }

    @Test
    public void shouldGet() {
        BooleanQueryValue value = TRUE;
        Assertions.assertThat(value).isNotNull().extracting(BooleanQueryValue::get)
                .isEqualTo(Boolean.TRUE);
    }

    @Test
    public void shouldEquals() {
        assertEquals(TRUE, TRUE);
        assertNotEquals(TRUE, FALSE);
        assertNotEquals(TRUE, Boolean.TRUE);
    }

    @Test
    public void shouldHashCode() {
        assertEquals(TRUE.hashCode(), TRUE.hashCode());
    }
}