package org.apache.diana.api.document;


public interface DocumentEntityManagerFactory {

    DocumentEntityManager getColumnEntityManager(String database);
}
