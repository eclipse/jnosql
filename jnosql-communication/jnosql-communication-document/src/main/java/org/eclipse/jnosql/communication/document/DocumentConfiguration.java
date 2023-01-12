/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.eclipse.jnosql.communication.document;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Settings;

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
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends DocumentConfiguration> T getConfiguration() {
        return (T) ServiceLoaderProvider.getUnique(DocumentConfiguration.class, () ->
                ServiceLoader.load(DocumentConfiguration.class));

    }

    /**
     * creates and returns a  {@link DocumentConfiguration} instance from {@link ServiceLoader}
     * for a particular provider implementation.
     *
     * @param <T>     the configuration type
     * @param service the particular provider
     * @return {@link DocumentConfiguration} instance
     * @throws jakarta.nosql.ProviderNotFoundException when the provider is not found
     * @throws jakarta.nosql.NonUniqueResultException  when there is more than one KeyValueConfiguration
     */
    static <T extends DocumentConfiguration> T getConfiguration(Class<T> service) {
        return ServiceLoaderProvider.getUnique(DocumentConfiguration.class,
                () -> ServiceLoader.load(DocumentConfiguration.class), service);
    }
}
