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


public class ConditionTest {

    @Test
    public void shouldReturnNameField() {
        assertEquals("__AND__", Condition.AND);
        assertEquals("__EQUALS__", Condition.EQUALS);
        assertEquals("__GREATER_EQUALS_THAN__", Condition.GREATER_EQUALS_THAN);
        assertEquals("__IN__", Condition.IN);
        assertEquals("__NOT__", Condition.NOT);
        assertEquals("__OR__", Condition.OR);
        assertEquals("__LESSAR_THAN__", Condition.LESSER_THAN);
    }
}