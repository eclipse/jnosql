/*
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
 */
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation is used to generate List, Set, Queue, and Map instances using a key-value database
 * obtained from a BucketManagerFactory. The annotation specifies the bucket name for the database.
 * An example demonstrating injection of various data structures using the {@link KeyValueDatabase} annotation.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * // Inject a List<String> instance from the "names" bucket in the key-value database.
 * @Inject
 * @KeyValueDatabase("names")
 * private List<String> names;
 *
 * // Inject a Set<String> instance from the "fruits" bucket in the key-value database.
 * @Inject
 * @KeyValueDatabase("fruits")
 * private Set<String> fruits;
 *
 * // Inject a Queue<String> instance from the "orders" bucket in the key-value database.
 * @Inject
 * @KeyValueDatabase("orders")
 * private Queue<String> orders;
 *
 * // Inject a Map<String, String> instance from the "orders" bucket in the key-value database.
 * @Inject
 * @KeyValueDatabase("orders")
 * private Map<String, String> map;
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Qualifier
public @interface KeyValueDatabase {

    /**
     * Specifies the name of the bucket in the key-value database that will be used for data storage.
     *
     * @return The name of the bucket.
     */
    @Nonbinding String value();
}
