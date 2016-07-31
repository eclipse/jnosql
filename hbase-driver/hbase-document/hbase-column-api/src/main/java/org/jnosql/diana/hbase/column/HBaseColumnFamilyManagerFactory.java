package org.jnosql.diana.hbase.column;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

import java.io.IOException;

class HBaseColumnFamilyManagerFactory implements ColumnFamilyManagerFactory {

    private final Configuration configuration;

    HBaseColumnFamilyManagerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ColumnFamilyManager getColumnEntityManager(String database) {
        try {
            Connection connection = ConnectionFactory.createConnection(configuration);
            Admin admin = connection.getAdmin();

            if (admin.tableExists(TableName.valueOf(database))) {
            } else {
                admin.createTable(new HTableDescriptor(TableName.valueOf(database)));
            }
            return new HBaseColumnFamilyManager(connection);
        } catch (IOException e) {
            throw new DianaHBaseException("A error happened when try to create ColumnFamilyManager", e);
        }
    }

    @Override
    public void close() {

    }


}
