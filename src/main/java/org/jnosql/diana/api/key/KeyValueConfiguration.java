/*
 *
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
 *
 */

package org.jnosql.diana.api.key;

/**
 * The diana configuration to create a {@link BucketManagerFactory}
 *
 * @param <T> the BucketManagerFactory type
 */
public interface KeyValueConfiguration<T extends BucketManagerFactory> {

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider
     * and then creates a {@link BucketManagerFactory} instance.
     *
     * @return a {@link BucketManagerFactory} instance
     */
    T get();
}
