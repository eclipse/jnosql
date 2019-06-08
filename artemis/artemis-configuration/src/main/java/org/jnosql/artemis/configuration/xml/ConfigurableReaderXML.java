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
package org.jnosql.artemis.configuration.xml;

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.configuration.Configurable;
import org.jnosql.artemis.configuration.ConfigurableReader;
import org.jnosql.artemis.configuration.ConfigurationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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

@Named("xml")
@ApplicationScoped
class ConfigurableReaderXML implements ConfigurableReader {

    private static final JAXBContext JAXB_CONTEX;

    private static final ThreadLocal<Unmarshaller> UNMARSHALLER;

    private static final Logger LOGGER = Logger.getLogger(ConfigurableReaderXML.class.getName());

    private final Map<String, List<Configurable>> cache = new ConcurrentHashMap<>();

    static {
        try {
            JAXB_CONTEX = JAXBContext.newInstance(ConfigurablesXML.class);
            UNMARSHALLER = ThreadLocal.withInitial(() -> {
                try {
                    return JAXB_CONTEX.createUnmarshaller();
                } catch (JAXBException e) {
                    throw new ConfigurationException("Error to load xml Unmarshaller context", e);
                }
            });
        } catch (JAXBException e) {
            throw new ConfigurationException("Error to load xml context", e);
        }

    }

    @Override
    public List<Configurable> read(Supplier<InputStream> stream, ConfigurationUnit annotation) {

        List<Configurable> configurations = cache.get(annotation.fileName());

        if (nonNull(configurations)) {
            LOGGER.fine("Loading the configuration file from the cache file: " + annotation.fileName());
            return configurations;
        }

        try {
            Unmarshaller unmarshaller = UNMARSHALLER.get();
            ConfigurablesXML configurablesXML = (ConfigurablesXML) unmarshaller.unmarshal(stream.get());
            List<Configurable> configurables = new ArrayList<>(ofNullable(configurablesXML.getConfigurations()).orElse(emptyList()));
            cache.put(annotation.fileName(), configurables);
            return configurables;
        } catch (JAXBException e) {
            throw new ConfigurationException("Error to read XML file:" + annotation.fileName(), e);
        }
    }
}
