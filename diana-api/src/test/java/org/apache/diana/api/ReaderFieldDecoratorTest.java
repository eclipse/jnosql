package org.apache.diana.api;

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
