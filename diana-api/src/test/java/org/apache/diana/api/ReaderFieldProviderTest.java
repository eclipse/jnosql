package org.apache.diana.api;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ReaderFieldProviderTest {

    private ReaderFieldProvider serviceLoader = ReaderFieldProvider.getInstance();


    @Test
    public void shouldConvert() {
        Number convert = serviceLoader.convert(Number.class, "10D");
        assertEquals(Double.valueOf(10D), convert);
    }

    @Test
    public void shoulCastObject() {
        Bean name = serviceLoader.convert(Bean.class, new Bean("name"));
        Assert.assertEquals(name.getClass(), Bean.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shoulReturnErrorWhenTypeIsNotSupported() {
        Bean name = serviceLoader.convert(Bean.class, "name");
    }

    @Test
    public void shouldReturnIfIsCompatible() {
        assertTrue(serviceLoader.hasSupport(Integer.class));
    }

    @Test
    public void shouldReturnIfIsNotCompatible() {
        assertFalse(serviceLoader.hasSupport(Bean.class));
    }


    class Bean {
        private String name;

        Bean(String name) {
            this.name = name;
        }
    }

}
