package org.jnosql.diana.hbase.column;

import org.jnosql.diana.api.column.ColumnConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class HBaseColumnConfigurationTest {


    @Test
    public void shouldCreatesColumnFamilyManagerFactory() {
        ColumnConfiguration configuration = new HBaseColumnConfiguration();
        assertNotNull(configuration.getManagerFactory());
    }

    @Test
    public void shouldCreatesColumnFamilyManagerFactoryFromConfiguration() {
        ColumnConfiguration configuration = new HBaseColumnConfiguration();
        assertNotNull(configuration.getManagerFactory());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorCreatesColumnFamilyManagerFactory() {
        new HBaseColumnConfiguration(null);
    }
}