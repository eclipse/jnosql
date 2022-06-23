/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping;


import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;

import javax.enterprise.util.AnnotationLiteral;
import java.util.Objects;

import static jakarta.nosql.mapping.DatabaseType.COLUMN;
import static jakarta.nosql.mapping.DatabaseType.DOCUMENT;
import static jakarta.nosql.mapping.DatabaseType.GRAPH;
import static jakarta.nosql.mapping.DatabaseType.KEY_VALUE;

/**
 * Utilitarian class to select the {@link Database}
 */
public final class DatabaseQualifier extends AnnotationLiteral<Database> implements Database {

    private static final DatabaseQualifier DEFAULT_DOCUMENT_PROVIDER = new DatabaseQualifier("", DOCUMENT);

    private static final DatabaseQualifier DEFAULT_COLUMN_PROVIDER = new DatabaseQualifier("", COLUMN);

    private static final DatabaseQualifier DEFAULT_KEY_VALUE_PROVIDER = new DatabaseQualifier("", KEY_VALUE);

    private static final DatabaseQualifier DEFAULT_GRAPH_PROVIDER = new DatabaseQualifier("", GRAPH);

    private final String provider;

    private final DatabaseType type;

    private DatabaseQualifier(String provider, DatabaseType type) {
        this.provider = provider;
        this.type = type;
    }

    @Override
    public DatabaseType value() {
        return type;
    }

    @Override
    public String provider() {
        return provider;
    }


    /**
     * Returns the qualifier filter with document type {@link DatabaseType#DOCUMENT}
     * and the nosql provider default
     *
     * @return the default document provider
     */
    public static DatabaseQualifier ofDocument() {
        return DEFAULT_DOCUMENT_PROVIDER;
    }

    /**
     * Returns the qualifier filter with document type {@link DatabaseType#DOCUMENT} and the
     * nosql provider defined
     *
     * @param provider the provider
     * @return the qualifier filter instance
     * @throws NullPointerException when provider is null
     */
    public static DatabaseQualifier ofDocument(String provider) {
        Objects.requireNonNull(provider, "provider is required");
        if (provider.trim().isEmpty()) {
            return DEFAULT_DOCUMENT_PROVIDER;
        }
        return new DatabaseQualifier(provider, DOCUMENT);
    }

    /**
     * Returns the qualifier filter with document type {@link DatabaseType#COLUMN}
     * and the nosql provider default
     *
     * @return the default column provider
     */
    public static DatabaseQualifier ofColumn() {
        return DEFAULT_COLUMN_PROVIDER;
    }

    /**
     * Returns the qualifier filter with document type {@link DatabaseType#COLUMN} and the
     * nosql provider defined
     *
     * @param provider the provider
     * @return the qualifier filter instance
     * @throws NullPointerException when provider is null
     */
    public static DatabaseQualifier ofColumn(String provider) {
        Objects.requireNonNull(provider, "provider is required");
        if (provider.trim().isEmpty()) {
            return DEFAULT_COLUMN_PROVIDER;
        }
        return new DatabaseQualifier(provider, COLUMN);
    }
    /**
     * Returns the qualifier filter with document type {@link DatabaseType#KEY_VALUE}
     * and the nosql provider default
     *
     * @return the default key-value provider
     */
    public static DatabaseQualifier ofKeyValue() {
        return DEFAULT_KEY_VALUE_PROVIDER;
    }

    /**
     * Returns the qualifier filter with document type {@link DatabaseType#KEY_VALUE} and the
     * nosql provider defined
     *
     * @param provider the provider
     * @return the qualifier filter instance
     * @throws NullPointerException when provider is null
     */
    public static DatabaseQualifier ofKeyValue(String provider) {
        Objects.requireNonNull(provider, "provider is required");
        if (provider.trim().isEmpty()) {
            return DEFAULT_KEY_VALUE_PROVIDER;
        }
        return new DatabaseQualifier(provider, KEY_VALUE);
    }

    /**
     * Returns the qualifier filter with document type {@link DatabaseType#GRAPH}
     * and the nosql provider default
     *
     * @return the default key-value provider
     */
    public static DatabaseQualifier ofGraph() {
        return DEFAULT_GRAPH_PROVIDER;
    }

    /**
     * Returns the qualifier filter with graph type {@link DatabaseType#GRAPH} and the
     * nosql provider defined
     *
     * @param provider the provider
     * @return the qualifier filter instance
     * @throws NullPointerException when provider is null
     */
    public static DatabaseQualifier ofGraph(String provider) {
        Objects.requireNonNull(provider, "provider is required");
        if (provider.trim().isEmpty()) {
            return DEFAULT_KEY_VALUE_PROVIDER;
        }
        return new DatabaseQualifier(provider, GRAPH);
    }
}