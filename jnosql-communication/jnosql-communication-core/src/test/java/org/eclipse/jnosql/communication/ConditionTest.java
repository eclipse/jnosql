/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.eclipse.jnosql.communication;


import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.Test;

import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.eclipse.jnosql.communication.Condition.GREATER_EQUALS_THAN;
import static org.eclipse.jnosql.communication.Condition.IN;
import static org.eclipse.jnosql.communication.Condition.LESSER_THAN;
import static org.eclipse.jnosql.communication.Condition.NOT;
import static org.eclipse.jnosql.communication.Condition.OR;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        assertEquals(AND, Condition.parse("_AND"));
        assertEquals(EQUALS,Condition.parse("_EQUALS"));
        assertEquals(GREATER_EQUALS_THAN,Condition.parse("_GREATER_EQUALS_THAN"));
        assertEquals(IN,Condition.parse("_IN"));
        assertEquals(NOT,Condition.parse("_NOT"));
        assertEquals(OR,Condition.parse("_OR"));
        assertEquals(LESSER_THAN,Condition.parse("_LESSER_THAN"));

    }
}