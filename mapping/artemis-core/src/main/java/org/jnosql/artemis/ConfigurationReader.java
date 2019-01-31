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

/**
 * Reader class that given the annotation returns {@link ConfigurationSettingsUnit}
 *
 * {@link ConfigurationUnit}
 */
public interface ConfigurationReader {


    /**
     * Reads the annotation and returns the ConfigurationUnit
     *
     * @param annotation         the annotation that has configuration unit
     * @param configurationClass the class to check the instance
     * @param <T>                the class type
     * @return a {@link ConfigurationSettingsUnit}
     * @throws NullPointerException   when either annotation or class are null
     * @throws ConfigurationException when the class does not found,
     *                                is not the same type of configuratioClass, when the file does not exist, when the unit is not found in the configuration file,
     *                                when there is ambiguous configuration
     */
    <T> ConfigurationSettingsUnit read(ConfigurationUnit annotation, Class<T> configurationClass);

    /**
     * Reads the annotation and returns the ConfigurationUnit
     *
     * @param annotation the annotation that has configuration unit
     * @param <T>        the class type
     * @return a {@link ConfigurationSettingsUnit}
     * @throws NullPointerException   when annotation is null
     * @throws ConfigurationException when the unit is not found in the configuration file,
     *                                when there is ambiguous configuration
     */
    <T> ConfigurationSettingsUnit read(ConfigurationUnit annotation);
}
