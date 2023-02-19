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
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.CommunicationException;
import org.eclipse.jnosql.communication.Settings;

import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * It is a function that reads from {@link Settings} and then creates a manager factory instance.
 * It should return a {@link NullPointerException} when the {@link Settings} parameter is null.
 *
 * @see DocumentManagerFactory
 * @see DocumentManager
 */
public interface DocumentConfiguration extends Function<Settings, DocumentManagerFactory> {

    /**
     * creates and returns a  {@link DocumentConfiguration}  instance from {@link ServiceLoader}
     *
     * @param <T> the configuration type
     * @return {@link DocumentConfiguration} instance
     */
    static <T extends DocumentConfiguration> T getConfiguration() {
        return (T) ServiceLoader.load(DocumentConfiguration.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .findFirst().orElseThrow(() -> new CommunicationException("No DocumentConfiguration implementation found!"));

    }

    /**
     * creates and returns a  {@link DocumentConfiguration} instance from {@link ServiceLoader}
     * for a particular provider implementation.
     *
     * @param <T>     the configuration type
     * @param type the particular provider
     * @return {@link DocumentConfiguration} instance
     */
    static <T extends DocumentConfiguration> T getConfiguration(Class<T> type) {
        return (T) ServiceLoader.load(DocumentConfiguration.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(type::isInstance)
                .findFirst().orElseThrow(() -> new CommunicationException("No DocumentConfiguration implementation found!"));
    }
}
