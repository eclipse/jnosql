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

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.configuration.Configurable;
import org.jnosql.artemis.configuration.ConfigurableReader;
import org.jnosql.artemis.configuration.ConfigurationException;
import org.jnosql.artemis.configuration.DefaultConfigurable;
import org.yaml.snakeyaml.Yaml;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

@Named("yaml")
@ApplicationScoped
class ConfigurableReaderYAML implements ConfigurableReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigurableReaderYAML.class.getName());
    private final Map<String, List<Configurable>> cache = new ConcurrentHashMap<>();

    @Override
    public List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation) {
        List<Configurable> cached = cache.get(annotation.fileName());
        if (nonNull(cached)) {
            LOGGER.info("Loading the configuration file from the cache file: " + annotation.fileName());
            return cached;
        }

        try {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(stream.get());
            List<Map<String, Object>> yamlConfigurations = (List<Map<String, Object>>) config.get("configurations");
            List<Configurable> configurations = new ArrayList<>();
            for (Map<String, Object> configuration : yamlConfigurations) {
                configurations.add(getConfigurable(configuration));
            }
            cache.put(annotation.fileName(), configurations);
            return configurations;
        } catch (Exception exp) {
            throw new ConfigurationException("Error to load Yaml file configuration", exp);
        }
    }

    private Configurable getConfigurable(Map<String, Object> configuration) {
        DefaultConfigurable configurable = new DefaultConfigurable();
        configurable.setDescription(getValue(configuration, "description"));
        configurable.setName(getValue(configuration, "name"));
        configurable.setProvider(getValue(configuration, "provider"));
        configurable.setSettings((Map<String, String>) configuration.get("settings"));
        return configurable;
    }

    private String getValue(Map<String, Object> configuration, String key) {
        return Optional.ofNullable(configuration.get(key))
                .map(Object::toString).orElse(null);
    }
}
