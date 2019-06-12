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
package org.jnosql.artemis.configuration;

import jakarta.nosql.mapping.ConfigurationUnit;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

/**
 * The reader of configurations
 */
public interface ConfigurableReader {

    /**
     * That reads the configurations and than returns {@link Configurable}
     * @param stream the stream
     * @param annotation the annotation
     * @return the configurations list
     * @throws NullPointerException when either stream or annotation are null
     * @throws ConfigurationException when has configuration problem
     */
    List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation);
}
