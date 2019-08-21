/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.column.spi;


import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import org.jnosql.artemis.util.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * It creates both a {@link ColumnTemplate} and a {@link ColumnTemplateAsync} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class ManagerConfigurationProducer {



    @Inject
    private ColumnConfigurationProducer configurationProducer;

    @ConfigurationUnit
    @Produces
    public ColumnFamilyManager get(InjectionPoint injectionPoint) {
        ColumnFamilyManagerFactory managerFactory = configurationProducer.get(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("The field database at ConfigurationUnit annotation is required");
        }
        return managerFactory.get(database);
    }

    @ConfigurationUnit
    @Produces
    public ColumnFamilyManagerAsync getAsync(InjectionPoint injectionPoint) {
        ColumnFamilyManagerAsyncFactory managerFactory = configurationProducer.getAsync(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("The field database at ConfigurationUnit annotation is required");
        }
        return managerFactory.getAsync(database);
    }
}
