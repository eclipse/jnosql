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
package org.jnosql.artemis;

import org.jnosql.diana.api.Settings;

import java.util.Optional;

/**
 * This interface represents the Configuration Unit information.
 */
public interface ConfigurationSettingsUnit {

    /**
     * Returns the unit-name from the configuration.
     * When a configuration file has more than one, the injection configuration unit must match {@link org.jnosql.artemis.ConfigurationUnit#name()}
     *
     * @return the name otherwise {@link Optional#empty()}
     * @see org.jnosql.artemis.ConfigurationUnit
     */
    Optional<String> getName();

    /**
     * Returns the description
     *
     * @return the description otherwise {@link Optional#empty()}
     */
    Optional<String> getDescription();

    /**
     * Returns the class provider
     *
     * @param <T> the class type
     * @return the provider class otherwise {@link Optional#empty()}
     */
    <T> Optional<Class<T>> getProvider();

    /**
     * Returns the settings from configuration
     *
     * @return the settings from configuration {@link Settings}
     */
    Settings getSettings();
}
