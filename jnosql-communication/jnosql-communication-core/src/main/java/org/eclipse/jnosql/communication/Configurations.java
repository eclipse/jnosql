/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 *
 */
package org.eclipse.jnosql.communication;

import java.util.function.Supplier;

/**
 * This enum contains common configurations that are frequently used for NoSQL databases.
 * It provides a standardized way to retrieve configuration keys using the {@link Supplier} interface,
 * which allows these properties to be fetched dynamically in different contexts.
 *
 * <p>Each constant in this enum represents a specific configuration option, such as connection details
 * (user, password, host) or other database-related settings (like encryption and pagination behavior).
 * By implementing {@link Supplier}, each enum constant can supply the associated property value directly
 * via the {@link #get()} method.</p>
 *
 * <p>Developers can reference these constants throughout the application to avoid hardcoding configuration
 * keys and ensure consistent access to NoSQL database properties. This is particularly useful for managing
 * complex or large-scale database configurations where multiple properties (e.g., hosts, pagination settings)
 * are involved.</p>
 */
public enum Configurations implements Supplier<String> {

    /**
     * Configuration for setting the user in a NoSQL database.
     * <p>This property is used to specify the username required for authenticating
     * the connection to the NoSQL database.</p>
     * <p>Example: <code>jakarta.nosql.user=admin</code></p>
     */
    USER("jakarta.nosql.user"),

    /**
     * Configuration for setting the password in a NoSQL database.
     * <p>This property is used in conjunction with the {@link #USER} configuration
     * to authenticate the database connection by providing the user’s password.</p>
     * <p>Example: <code>jakarta.nosql.password=secret</code></p>
     */
    PASSWORD("jakarta.nosql.password"),

    /**
     * Host configuration for connecting to the NoSQL database.
     * <p>This property allows setting multiple hosts by using a numbered suffix (e.g., host-1, host-2).
     * This is useful for distributed databases or in cases where multiple instances or replicas
     * of the database are running.</p>
     * <p>Example: <code>jakarta.nosql.host-1=localhost</code>, <code>jakarta.nosql.host-2=remote-host</code></p>
     */
    HOST("jakarta.nosql.host"),

    /**
     * Configuration to enable encryption settings for NoSQL database connections.
     * <p>This property is used to configure encryption settings, which are critical
     * for securing data in transit or at rest within the NoSQL database. It defines the encryption
     * protocols or keys that should be applied.</p>
     * <p>Example: <code>jakarta.nosql.settings.encryption=AES256</code></p>
     */
    ENCRYPTION("jakarta.nosql.settings.encryption"),

    /**
     * Configuration to enable cursor-based pagination with multiple sorting in NoSQL databases.
     * <p>This property allows enabling cursor pagination with support for multiple sorting fields.
     * By default, multiple sorting is disabled due to potential inconsistencies in NoSQL databases when
     * sorting by multiple fields that contain duplicate values. Enabling this option allows users to
     * explicitly manage multi-field sorting during cursor-based pagination.</p>
     * <p>To enable, set: <code>org.eclipse.jnosql.pagination.cursor=true</code></p>
     */
    CURSOR_PAGINATION_MULTIPLE_SORTING("org.eclipse.jnosql.pagination.cursor");

    private final String configuration;

    Configurations(String configuration) {
        this.configuration = configuration;
    }


    @Override
    public String get() {
        return configuration;
    }
}
