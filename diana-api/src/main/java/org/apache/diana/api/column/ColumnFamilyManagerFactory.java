package org.apache.diana.api.column;


public interface ColumnFamilyManagerFactory extends AutoCloseable {

    ColumnFamilyManager getColumnEntityManager(String database);
}
