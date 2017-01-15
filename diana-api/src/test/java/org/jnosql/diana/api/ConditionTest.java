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

import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;
import static org.junit.Assert.*;


public class ConditionTest {

    @Test
    public void shouldReturnNameField() {
        assertEquals("_AND", AND.getNameField());
        assertEquals("_EQUALS", EQUALS.getNameField());
        assertEquals("_GREATER_EQUALS_THAN", GREATER_EQUALS_THAN.getNameField());
        assertEquals("_IN", IN.getNameField());
        assertEquals("_NOT", NOT.getNameField());
        assertEquals("_OR", OR.getNameField());
        assertEquals("_LESSER_THAN", LESSER_THAN.getNameField());
    }

    @Test
    public void shouldParser() {
        assertEquals(AND,Condition.parse("__AND__"));


    }
}