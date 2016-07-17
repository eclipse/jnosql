package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.ColumnEntityManagerFactory;

import java.util.List;

class CassandraDocumentEntityManagerFactory implements ColumnEntityManagerFactory {

    private final Cluster cluster;

    public CassandraDocumentEntityManagerFactory(final Cluster cluster, List<String> queries) {
        this.cluster = cluster;
        runIniticialQuery(queries);
    }

    public void runIniticialQuery(List<String> queries) {
        Session session = cluster.connect();
        queries.forEach(session::execute);
        session.close();
    }

    @Override
    public ColumnEntityManager getColumnEntityManager(String database) {
        return new CassandraDocumentEntityManager(cluster.connect(database), database);
    }

    @Override
    public void close() throws Exception {
        cluster.close();
    }

    Cluster getCluster() {
        return cluster;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CassandraDocumentEntityManagerFactory{");
        sb.append("cluster=").append(cluster);
        sb.append('}');
        return sb.toString();
    }
}
