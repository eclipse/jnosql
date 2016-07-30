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

package org.jnosql.diana.cassandra.column;

import com.datastax.driver.core.Cluster;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class CassandraDocumentEntityManagerFactoryTest {

    private ColumnFamilyManagerFactory subject;

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
        ColumnFamilyManager columnEntityManager = subject.getColumnEntityManager(Constants.KEY_SPACE);
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