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
package org.eclipse.jnosql.communication.query.method;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.query.ValueType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodParamQueryValueTest {

    @Test
    public void shouldReturnType() {
        MethodParamQueryValue param = new MethodParamQueryValue("name");
        Assertions.assertThat(param).isNotNull()
                .extracting(MethodParamQueryValue::type)
                .isNotNull().isEqualTo(ValueType.PARAMETER);
    }
    @Test
    public void shouldCreateInstance() {
        MethodParamQueryValue param = new MethodParamQueryValue("name");
        Assertions.assertThat(param).isNotNull()
                .extracting(MethodParamQueryValue::get)
                .isNotNull();

    }

    @Test
    public void shouldEquals() {
        MethodParamQueryValue param = new MethodParamQueryValue("name");
        assertEquals(param, param);
    }

    @Test
    public void shouldHashCode() {
        MethodParamQueryValue param = new MethodParamQueryValue("name");
        assertEquals(param.hashCode(), param.hashCode());
    }
}