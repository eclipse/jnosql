package org.jnosql.diana.hbase.column;

import org.jnosql.diana.api.column.ColumnConfiguration;
import org.junit.Assert;
import org.junit.Test;


public class HBaseColumnConfigurationTest {


    @Test
    public void shouldCreatesColumnFamilyManagerFactory() {
        ColumnConfiguration configuration = new HBaseColumnConfiguration();
        Assert.assertNotNull(configuration.getManagerFactory());
    }

    @Test
    public void shouldCreatesColumnFamilyManagerFactoryFromConfiguration() {
        ColumnConfiguration configuration = new HBaseColumnConfiguration();
        Assert.assertNotNull(configuration.getManagerFactory());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorCreatesColumnFamilyManagerFactory() {
        new HBaseColumnConfiguration(null);
    }
}