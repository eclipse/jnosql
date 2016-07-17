package org.apache.diana.cassandra.document;

import com.datastax.driver.core.Cluster;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.ColumnEntityManagerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class CassandraDocumentEntityManagerFactoryTest {

    private ColumnEntityManagerFactory subject;

    @Before
    public void setUp() {
        Map<String, String> configurations = new HashMap<>();
        configurations.put("cassandra-hoster-1", "172.17.0.2");
        configurations.put("cassandra-initial-query-1", " CREATE KEYSPACE IF NOT EXISTS newKeySpace WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};");
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        subject = cassandraConfiguration.getManagerFactory(configurations);
    }

    @Test
    public void shouldReturnEntityManager() throws Exception {
        ColumnEntityManager columnEntityManager = subject.getColumnEntityManager("newKeySpace");
        assertNotNull(columnEntityManager);
    }

    @Test
    public void shouldCloseNode() throws Exception {
        subject.close();
        CassandraDocumentEntityManagerFactory cassandraDocumentEntityManagerFactory = CassandraDocumentEntityManagerFactory.class.cast(subject);
        Cluster cluster = cassandraDocumentEntityManagerFactory.getCluster();
        assertTrue(cluster.isClosed());
    }

}