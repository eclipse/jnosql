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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.jnosql.diana.api.column.ColumnConfiguration;

public class CassandraConfiguration implements ColumnConfiguration<CassandraDocumentEntityManagerFactory> {

    private static final String CASSANDRA_FILE_CONFIGURATION = "diana-cassandra.properties";

    public CassandraDocumentEntityManagerFactory getManagerFactory(Map<String, String> configurations) {
        Objects.requireNonNull(configurations);
        List<String> nodes = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-hoster"))
                .map(configurations::get).collect(Collectors.toList());
        Cluster.Builder builder = Cluster.builder();
        nodes.forEach(builder::addContactPoint);
        List<String> queries = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-initial-query"))
                .sorted().map(configurations::get).collect(Collectors.toList());
        String numThreadsServer = Integer.toString(Runtime.getRuntime().availableProcessors());
        String numTreads = configurations.getOrDefault("cassandra-threads-number", numThreadsServer);
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.valueOf(numTreads));
        return new CassandraDocumentEntityManagerFactory(builder.build(), queries, executorService);
    }

    @Override
    public CassandraDocumentEntityManagerFactory getManagerFactory() {

        try {
            Properties properties = new Properties();
            InputStream stream = CassandraConfiguration.class.getClassLoader()
                    .getResourceAsStream(CASSANDRA_FILE_CONFIGURATION);
            properties.load(stream);
            Map<String, String> collect = properties.keySet().stream().collect(Collectors
                    .toMap(Object::toString, s -> properties.get(s).toString()));
            return getManagerFactory(collect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
