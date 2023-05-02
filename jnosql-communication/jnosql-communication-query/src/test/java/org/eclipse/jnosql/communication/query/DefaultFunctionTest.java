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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultFunctionTest {

    @Test
    public void shouldReturnType() {
        Function function = DefaultFunction.of("sum", new Object[]{1, 2});
        assertThat(function).isNotNull()
                .extracting(Function::name).isEqualTo("sum");


    }

    @Test
    public void shouldReturnValue(){
        Function function = DefaultFunction.of("sum", new Object[]{1, 2});
        assertThat(function).extracting(Function::params)
                .isEqualTo(new Object[]{1, 2});
    }

    @Test
    public void shouldReturnEquals(){
        Function f = DefaultFunction.of("sum", new Object[]{1, 2});
        Function f2 = DefaultFunction.of("sum", new Object[]{1, 2});
        assertEquals(f, f2);
    }

    @Test
    public void shouldReturnHashCode() {
        Function f = DefaultFunction.of("sum", new Object[]{1, 2});
        Function f2 = DefaultFunction.of("sum", new Object[]{1, 2});
        assertEquals(f.hashCode(), f2.hashCode());
    }
}