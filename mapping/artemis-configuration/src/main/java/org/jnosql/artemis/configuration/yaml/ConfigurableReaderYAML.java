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
import org.yaml.snakeyaml.Yaml;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        List<Configurable> configurations = cache.get(annotation.fileName());

        if (nonNull(configurations)) {
            LOGGER.info("Loading the configuration file from the cache file: " + annotation.fileName());
            return configurations;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> config = yaml.load(stream.get());
//            final ConfigurablesYAML yaml = mapper.readValue(stream.get(), ConfigurablesYAML.class);
//            List<Configurable> configurables = new ArrayList<>(ofNullable(yaml.getConfigurations()).orElse(emptyList()));
//            cache.put(annotation.fileName(), configurables);
        return Collections.emptyList();
    }
}
