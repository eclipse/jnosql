package org.apache.diana.api.column;


public interface ColumnEntityManagerFactory extends AutoCloseable {

    ColumnEntityManager getColumnEntityManager(String database);
}
