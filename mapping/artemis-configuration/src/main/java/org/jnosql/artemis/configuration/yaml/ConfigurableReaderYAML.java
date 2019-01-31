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
 *   Daniel Cunha <dcunha@tomitribe.com>
 */
package org.jnosql.artemis.configuration.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.configuration.Configurable;
import org.jnosql.artemis.configuration.ConfigurableReader;
import org.jnosql.artemis.configuration.ConfigurationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Named("yaml")
@ApplicationScoped
class ConfigurableReaderYAML implements ConfigurableReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigurableReaderYAML.class.getName());
    private final Map<String, List<Configurable>> cache = new ConcurrentHashMap<>();
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Override
    public List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation) {
        List<Configurable> configurations = cache.get(annotation.fileName());

        if (nonNull(configurations)) {
            LOGGER.info("Loading the configuration file from the cache file: " + annotation.fileName());
            return configurations;
        }
        try {
            final ConfigurablesYAML yaml = mapper.readValue(stream.get(), ConfigurablesYAML.class);
            List<Configurable> configurables = new ArrayList<>(ofNullable(yaml.getConfigurations()).orElse(emptyList()));
            cache.put(annotation.fileName(), configurables);
            return configurables;
        } catch (IOException exception) {
            throw new ConfigurationException("An error when read the YAML file: " + annotation.fileName()
                    , exception);
        }
    }
}
