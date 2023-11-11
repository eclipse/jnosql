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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultArrayQueryValueTest {

    @Test
    void shouldReturnArrayType() {
        ArrayQueryValue array = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE});
        assertThat(array).isNotNull();
        ValueType type = array.type();
        assertThat(type).isEqualTo(ValueType.ARRAY);
    }

    @Test
    void shouldReturnArrayValue() {
        ArrayQueryValue array = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        assertThat(array.get()).containsExactly(BooleanQueryValue.FALSE, BooleanQueryValue.TRUE);
    }

    @Test
    void shouldEquals(){
        ArrayQueryValue array = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        ArrayQueryValue arrayB = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        Assertions.assertEquals(arrayB, array);
        Assertions.assertEquals(array, array);
        Assertions.assertNotEquals(array, "array");
    }

    @Test
    void shouldHashCode(){
        ArrayQueryValue array = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        ArrayQueryValue arrayB = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        Assertions.assertEquals(arrayB.hashCode(), array.hashCode());
    }

    @Test
    void shouldToString(){
        ArrayQueryValue array = DefaultArrayQueryValue.of(new QueryValue<?>[]{
                BooleanQueryValue.FALSE, BooleanQueryValue.TRUE});
        assertThat(array.toString()).isEqualTo("[BooleanQueryValue{value=false}, BooleanQueryValue{value=true}]");
    }
}