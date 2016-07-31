package org.jnosql.diana.hbase.column;


import org.apache.hadoop.conf.Configuration;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

class HBaseColumnFamilyManagerFactory implements ColumnFamilyManagerFactory {

    private final Configuration configuration;

    HBaseColumnFamilyManagerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ColumnFamilyManager getColumnEntityManager(String database) {
        return null;
    }

    @Override
    public void close() {

    }


}
