package org.jnosql.diana.hbase.column;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.jnosql.diana.api.column.ColumnConfiguration;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

import java.util.Map;

public class HbaseConfiguration implements ColumnConfiguration {

    private final Configuration configuration;

    public HbaseConfiguration() {
        this.configuration = HBaseConfiguration.create();
    }

    public HbaseConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ColumnFamilyManagerFactory getManagerFactory(Map<String, String> configurations) {
        throw new IllegalArgumentException("It isn't implemented yet");
    }

    @Override
    public ColumnFamilyManagerFactory getManagerFactory() {
        return null;
    }
}
