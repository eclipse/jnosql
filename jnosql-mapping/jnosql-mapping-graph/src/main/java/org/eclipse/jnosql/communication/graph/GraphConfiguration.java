/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.communication.CommunicationException;
import org.eclipse.jnosql.communication.Settings;

import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * The Configuration that creates an instance of {@link Graph} that given a {@link Settings} make an  {@link Graph} instance.
 */
public interface GraphConfiguration extends Function<Settings, Graph> {


    /**
     * creates and returns a  {@link GraphConfiguration}  instance from {@link ServiceLoader}
     *
     * @param <T> the configuration type
     * @return {@link GraphConfiguration} instance
     */
    static <T extends GraphConfiguration> T getConfiguration() {
        return (T) ServiceLoader.load(GraphConfiguration.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .findFirst().orElseThrow(() -> new CommunicationException("It does not find GraphConfiguration"));
    }

    /**
     * creates and returns a  {@link GraphConfiguration} instance from {@link ServiceLoader}
     * for a particular provider implementation.
     *
     * @param <T>     the configuration type
     * @param type the particular provider
     * @return {@link GraphConfiguration} instance
     */
    static <T extends GraphConfiguration> T getConfiguration(Class<T> type) {
        return (T) ServiceLoader.load(GraphConfiguration.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(type::isInstance)
                .findFirst().orElseThrow(() -> new CommunicationException("It does not find GraphConfiguration"));
    }
}
