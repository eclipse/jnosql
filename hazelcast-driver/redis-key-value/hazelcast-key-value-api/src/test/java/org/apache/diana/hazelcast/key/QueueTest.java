package org.apache.diana.hazelcast.key;


import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.apache.diana.hazelcast.key.model.LineBank;
import org.apache.diana.hazelcast.key.util.KeyValueEntityManagerFactoryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Queue;

import static org.junit.Assert.*;


public class QueueTest {


    private KeyValueEntityManagerFactory keyValueEntityManagerFactory;

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
