package org.apache.diana.api.column;


public interface ColumnEntityManagerFactory {

    ColumnEntityManager getColumnEntityManager(String database);
}
