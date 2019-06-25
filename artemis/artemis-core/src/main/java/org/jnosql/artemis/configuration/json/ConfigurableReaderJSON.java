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
package org.jnosql.artemis.configuration.json;

import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.configuration.Configurable;
import jakarta.nosql.mapping.configuration.ConfigurableReader;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import org.jnosql.artemis.configuration.DefaultConfigurable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

/**
 * The {@link ConfigurableReader} to JSON
 */

@Named("json")
@ApplicationScoped
class ConfigurableReaderJSON implements ConfigurableReader {

    private static final Jsonb JSONB = JsonbBuilder.create();
    private static final Logger LOGGER = Logger.getLogger(ConfigurableReaderJSON.class.getName());
    private static final Type TYPE = new ArrayList<DefaultConfigurable>() {}.getClass().getGenericSuperclass();
    private final Map<String, List<Configurable>> cache = new ConcurrentHashMap<>();


    @Override
    public List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation) {
        List<Configurable> configurations = cache.get(annotation.fileName());

        if (nonNull(configurations)) {
            LOGGER.info("Loading the configuration file from the cache file: " + annotation.fileName());
            return configurations;
        }
        try {
            configurations = JSONB.fromJson(stream.get(), TYPE);
            cache.put(annotation.fileName(), configurations);
            return configurations;
        } catch (Exception exception) {
            throw new ConfigurationException("An error when read the JSON file: " + annotation.fileName()
                    , exception);
        }
    }
}
