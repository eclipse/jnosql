package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Cluster;
import org.apache.diana.api.document.DocumentEntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CassandraConfiguration {

    public DocumentEntityManagerFactory getConfiguration(Map<String, String> configurations){
        Objects.requireNonNull(configurations);
        List<String> nodes = configurations.keySet().stream().filter(s -> s.startsWith("cassandra-hoster-1"))
                .map(configurations::get).collect(Collectors.toList());
        Cluster.Builder builder = Cluster.builder();
        nodes.forEach(builder::addContactPoint);
        return new CassandraDocumentEntityManagerFactory(builder.build());
    }
}
