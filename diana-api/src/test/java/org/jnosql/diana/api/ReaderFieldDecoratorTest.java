/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class ReaderFieldDecoratorTest {

    private ReaderFieldDecorator serviceLoader = ReaderFieldDecorator.getInstance();


    @Test
    public void shouldConvert() {
        Number convert = serviceLoader.read(Number.class, "10D");
        assertEquals(10D, convert);
    }

    @Test
    public void shoulCastObject() {
        Bean name = serviceLoader.read(Bean.class, new Bean("name"));
        Assert.assertEquals(name.getClass(), Bean.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shoulReturnErrorWhenTypeIsNotSupported() {
        Bean name = serviceLoader.read(Bean.class, "name");
    }

    @Test
    public void shouldReturnIfIsCompatible() {
        assertTrue(serviceLoader.isCompatible(Integer.class));
    }

    @Test
    public void shouldReturnIfIsNotCompatible() {
        assertFalse(serviceLoader.isCompatible(Bean.class));
    }


    class Bean {
        private String name;

        Bean(String name) {
            this.name = name;
        }
    }

}
