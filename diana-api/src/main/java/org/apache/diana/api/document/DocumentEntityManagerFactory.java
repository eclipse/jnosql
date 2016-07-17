package org.apache.diana.api.document;


public interface DocumentEntityManagerFactory extends AutoCloseable {

    DocumentEntityManager getColumnEntityManager(String database);
}
