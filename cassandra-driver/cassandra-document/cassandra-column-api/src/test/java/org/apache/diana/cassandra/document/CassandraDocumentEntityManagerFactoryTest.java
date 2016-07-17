package org.apache.diana.cassandra.document;

import com.datastax.driver.core.Cluster;
import org.apache.diana.api.document.DocumentEntityManager;
import org.apache.diana.api.document.DocumentEntityManagerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by otaviojava on 17/07/16.
 */
public class CassandraDocumentEntityManagerFactoryTest {

    private DocumentEntityManagerFactory subject;

    @Before
    public void setUp() {
        Map<String, String> configurations = new HashMap<>();
        configurations.put("cassandra-hoster-1", "172.17.0.2");
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        subject = cassandraConfiguration.getConfiguration(configurations);
    }

    @Test
    public void shouldReturnEntityManager() throws Exception {
        DocumentEntityManager columnEntityManager = subject.getColumnEntityManager("newKeySpace");
        Assert.assertNotNull(columnEntityManager);
    }

    @Test
    public void shouldCloseNode() throws Exception {
        subject.close();
        CassandraDocumentEntityManagerFactory cassandraDocumentEntityManagerFactory = CassandraDocumentEntityManagerFactory.class.cast(subject);
        Cluster cluster = cassandraDocumentEntityManagerFactory.getCluster();
        assertTrue(cluster.isClosed());
    }

}