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

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringQueryValueTest {

    @Test
    public void shouldReturnType() {
        StringQueryValue string = new StringQueryValue("text");
        Assertions.assertThat(string).isNotNull()
                .extracting(StringQueryValue::type)
                .isEqualTo(ValueType.STRING);
    }


    @Test
    public void shouldReturnValue() {
        StringQueryValue string = new StringQueryValue("text");
        Assertions.assertThat(string).isNotNull()
                .extracting(StringQueryValue::get)
                .isEqualTo("text");
    }

    @Test
    public void shouldEquals() {
        assertEquals(new StringQueryValue("text"), new StringQueryValue("text"));
    }

    @Test
    public void shouldHasCode() {
        assertEquals(new StringQueryValue("text").hashCode()
                , new StringQueryValue("text").hashCode());

    }
}