package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Cluster;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.ColumnEntityManagerFactory;

class CassandraDocumentEntityManagerFactory implements ColumnEntityManagerFactory {

    private final Cluster cluster;

    public CassandraDocumentEntityManagerFactory(final Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public ColumnEntityManager getColumnEntityManager(String database) {
        return null;
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
