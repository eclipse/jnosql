package org.apache.diana.cassandra.column;


import org.apache.diana.api.column.ColumnFamilyManagerFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class CassandraConfigurationTest {

    @Test
    public void shoudlCreateDocumentEntityManagerFactory() {
        Map<String, String> configurations = new HashMap<>();
        configurations.put("cassandra-hoster-1", "172.17.0.2");
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        ColumnFamilyManagerFactory entityManagerFactory = cassandraConfiguration.getManagerFactory(configurations);
        assertNotNull(entityManagerFactory);
    }

    @Test
    public void shoudlCreateDocumentEntityManagerFactoryFromFile() {
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        ColumnFamilyManagerFactory entityManagerFactory = cassandraConfiguration.getManagerFactory();
        assertNotNull(entityManagerFactory);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenMapIsNull() {
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        cassandraConfiguration.getManagerFactory(null);
    }
}
