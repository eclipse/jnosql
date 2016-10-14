package org.jnosql.diana.api.reader;

import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.TypeReferenceReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StreamTypeReferenceReaderTest {

    private TypeReferenceReader referenceReader = new StreamTypeReferenceReader();

    @Test
    public void shouldBeCompatible() {

        assertTrue(referenceReader.isCompatible(new TypeReference<Stream<String>>(){}));
        assertTrue(referenceReader.isCompatible(new TypeReference<Stream<Long>>(){}));

    }


    @Test
    public void shouldNotBeCompatible() {
        assertFalse(referenceReader.isCompatible(new TypeReference<ArrayList<BigDecimal>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<String>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Set<String>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<List<List<String>>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Queue<String>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Map<Integer, String>>(){}));
    }


    @Test
    public void shouldConvert() {
        Stream<String> stream = referenceReader.convert(new TypeReference<Stream<String>>() {}, "123");
        assertEquals("123", stream.findAny().get());
    }
}