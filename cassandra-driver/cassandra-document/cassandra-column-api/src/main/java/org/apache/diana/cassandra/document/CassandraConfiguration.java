package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Cluster;
import org.apache.diana.api.column.ColumnEntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CassandraConfiguration {

    public ColumnEntityManagerFactory getManagerFactory(Map<String, String> configurations) {
        Objects.requireNonNull(configurations);
        List<String> nodes = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-hoster"))
                .map(configurations::get).collect(Collectors.toList());
        Cluster.Builder builder = Cluster.builder();
        nodes.forEach(builder::addContactPoint);
        List<String> queries = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-initial-query")).map(configurations::get).collect(Collectors.toList());
        return new CassandraDocumentEntityManagerFactory(builder.build(), queries);
    }
}
