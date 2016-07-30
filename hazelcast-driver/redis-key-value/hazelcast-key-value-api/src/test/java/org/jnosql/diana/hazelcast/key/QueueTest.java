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

package org.jnosql.diana.hazelcast.key;


import org.jnosql.diana.api.key.BucketManagerFactory;
import org.jnosql.diana.hazelcast.key.model.LineBank;
import org.jnosql.diana.hazelcast.key.util.KeyValueEntityManagerFactoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Queue;

import static org.junit.Assert.*;


public class QueueTest {


    private BucketManagerFactory keyValueEntityManagerFactory;

    private Queue<LineBank> lineBank;

    @Before
    public void init() {
        keyValueEntityManagerFactory =  KeyValueEntityManagerFactoryUtils.get();
        lineBank = keyValueEntityManagerFactory.getQueue("physical-bank", LineBank.class);
    }

    @Test
    public void shouldPushInTheLine() {
        assertTrue(lineBank.add(new LineBank("Otavio", 25)));
        assertTrue(lineBank.size() ==1);
        LineBank otavio = lineBank.poll();
        assertEquals(otavio.getPerson().getName(), "Otavio");
        assertNull(lineBank.poll());
        assertTrue(lineBank.isEmpty());
    }

    @Test
    public void shouldPeekInTheLine() {
        lineBank.add(new LineBank("Otavio", 25));
        LineBank otavio = lineBank.peek();
        assertNotNull(otavio);
        assertNotNull(lineBank.peek());
        LineBank otavio2 = lineBank.remove();
        assertEquals(otavio.getPerson().getName(), otavio2.getPerson().getName());
        boolean happendException = false;
        try {
            lineBank.remove();
        }catch(NoSuchElementException e) {
            happendException = true;
        }
        assertTrue(happendException);
    }

    @Test
    public void shouldElementInTheLine() {
        lineBank.add(new LineBank("Otavio", 25));
        assertNotNull(lineBank.element());
        assertNotNull(lineBank.element());
        lineBank.remove(new LineBank("Otavio", 25));
        boolean happendException = false;
        try {
            lineBank.element();
        }catch(NoSuchElementException e) {
            happendException = true;
        }
        assertTrue(happendException);
    }
    @SuppressWarnings("unused")
    @Test
    public void shouldIterate() {
        lineBank.add(new LineBank("Otavio", 25));
        lineBank.add(new LineBank("Gama", 26));
        int count = 0;
        for (LineBank line: lineBank) {
            count++;
        }
        assertTrue(count == 2);
        lineBank.remove();
        lineBank.remove();
        count = 0;
        for (LineBank line: lineBank) {
            count++;
        }
        assertTrue(count == 0);
    }
    @After
    public void dispose() {
        lineBank.clear();
    }
}
