package org.apache.diana.api.column;

/**
 * {@link ColumnFamilyManager} factory.
 * When the application has finished using the column family manager factory, and/or at application shutdown, the application should close the column family manager factory.
 */
public interface ColumnFamilyManagerFactory extends AutoCloseable {

    /**
     * Creates a {@link ColumnFamilyManager} from database's name
     * @param database a database name
     * @return a new {@link ColumnFamilyManager} instance
     */
    ColumnFamilyManager getColumnEntityManager(String database);
}
