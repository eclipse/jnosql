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

import static org.assertj.core.api.Assertions.assertThat;

class DefaultQueryValueTest {

    @Test
    public void shouldCreateInstance(){
        ParamQueryValue param = new DefaultQueryValue("name");
        assertThat(param).isNotNull()
                .extracting(ParamQueryValue::type)
                .isEqualTo(ValueType.PARAMETER);
    }

    @Test
    public void shouldReturnType(){
        ParamQueryValue param = new DefaultQueryValue("name");
        assertThat(param).isNotNull()
                .extracting(ParamQueryValue::type)
                .isEqualTo(ValueType.PARAMETER);
    }

    @Test
    public void shouldReturnValue(){
        ParamQueryValue param = new DefaultQueryValue("name");
        assertThat(param).isNotNull()
                .extracting(ParamQueryValue::get)
                .isEqualTo("name");
    }

    @Test
    public void shouldEquals(){
        ParamQueryValue param = new DefaultQueryValue("name");
        ParamQueryValue paramB = new DefaultQueryValue("name");
        Assertions.assertThat(param).isEqualTo(paramB);
    }

    @Test
    public void shouldHashCode(){
        ParamQueryValue param = new DefaultQueryValue("name");
        ParamQueryValue paramB = new DefaultQueryValue("name");
        Assertions.assertThat(param.hashCode()).isEqualTo(paramB.hashCode());
    }
}