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
package org.jnosql.diana.document;

import org.jnosql.diana.Settings;

/**
 * The diana configuration to create a {@link DocumentCollectionManagerFactory}
 *
 * @param <A> the type {@link DocumentCollectionManagerAsyncFactory}
 */
public interface DocumentConfigurationAsync<A extends DocumentCollectionManagerAsyncFactory> {

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider and then creates a
     * {@link DocumentCollectionManagerAsyncFactory} instance.
     *
     * @return a {@link DocumentCollectionManagerAsyncFactory} instance
     */
    A getAsync();

    /**
     * Reads configuration from the {@link Settings} instance, the parameters are defined by NoSQL
     * provider, then creates a {@link DocumentCollectionManagerAsyncFactory} instance.
     *
     * @param settings the settings
     * @return a {@link DocumentCollectionManagerAsyncFactory}
     * @throws NullPointerException when settings is null
     * @see Settings
     * @see Settings {@link java.util.Map}
     */
    A getAsync(Settings settings);
}
