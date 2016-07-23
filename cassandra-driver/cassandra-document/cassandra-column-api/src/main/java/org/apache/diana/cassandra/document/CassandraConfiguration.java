package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Cluster;
import org.apache.diana.api.column.ColumnConfiguration;
import org.apache.diana.api.column.ColumnFamilyManagerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CassandraConfiguration implements ColumnConfiguration {

    private static final String CASSANDRA_FILE_CONFIGURATION = "diana-cassandra.properties";

    @Override
    public ColumnFamilyManagerFactory getManagerFactory(Map<String, String> configurations) {
        Objects.requireNonNull(configurations);
        List<String> nodes = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-hoster"))
                .map(configurations::get).collect(Collectors.toList());
        Cluster.Builder builder = Cluster.builder();
        nodes.forEach(builder::addContactPoint);
        List<String> queries = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-initial-query")).sorted().map(configurations::get).collect(Collectors.toList());
        String numThreadsServer = Integer.toString(Runtime.getRuntime().availableProcessors());
        String numTreads = configurations.getOrDefault("cassandra-threads-number", numThreadsServer);
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.valueOf(numTreads));
        return new CassandraDocumentEntityManagerFactory(builder.build(), queries, executorService);
    }

    @Override
    public ColumnFamilyManagerFactory getManagerFactory() {

        try {
            Properties properties = new Properties();
            InputStream stream = CassandraConfiguration.class.getClassLoader().getResourceAsStream(CASSANDRA_FILE_CONFIGURATION);
            properties.load(stream);
            Map<String, String> collect = properties.keySet().stream().collect(Collectors.toMap(Object::toString, s -> properties.get(s).toString()));
            return getManagerFactory(collect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
