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

import static org.junit.jupiter.api.Assertions.*;

class NumberQueryValueTest {

    @Test
    public void shouldReturnType() {
        NumberQueryValue queryValue = new NumberQueryValue(10);

        Assertions.assertThat(queryValue.type())
                .isEqualTo(ValueType.NUMBER);
    }

    @Test
    public void shouldReturnGet() {
        NumberQueryValue queryValue = new NumberQueryValue(10);

        Assertions.assertThat(queryValue.get())
                .isEqualTo(10);
    }

    @Test
    public void shouldEquals() {
        assertEquals(new NumberQueryValue(10), new NumberQueryValue(10));

    }

    @Test
    public void shouldHashCode() {
        assertEquals(new NumberQueryValue(10).hashCode(), new NumberQueryValue(10).hashCode());
    }

}