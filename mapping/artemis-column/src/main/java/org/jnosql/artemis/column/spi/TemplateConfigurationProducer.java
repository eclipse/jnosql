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

import org.jnosql.artemis.ConfigurationException;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.artemis.column.ColumnTemplateAsyncProducer;
import org.jnosql.artemis.column.ColumnTemplateProducer;
import org.jnosql.artemis.util.StringUtils;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsyncFactory;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * It creates both a {@link ColumnTemplate} and a {@link ColumnTemplateAsync} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class TemplateConfigurationProducer {


    @Inject
    private ColumnConfigurationProducer configurationProducer;

    @Inject
    private ColumnTemplateProducer producer;

    @Inject
    private ColumnTemplateAsyncProducer asyncProducer;


    @ConfigurationUnit
    @Produces
    public ColumnTemplate getTemplate(InjectionPoint injectionPoint) {
        ColumnFamilyManagerFactory<?> managerFactory = configurationProducer.getColumnConfiguration(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint, injectionPoint.getAnnotated());
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("To create a DocumentTemplate from a ConfigurationUnit the database field is required");
        }
        ColumnFamilyManager manager = managerFactory.get(database);
        return producer.get(manager);
    }

    @ConfigurationUnit
    @Produces
    public ColumnTemplateAsync getTemplateAsync(InjectionPoint injectionPoint) {
        ColumnFamilyManagerAsyncFactory<?> managerFactory = configurationProducer.getColumnConfigurationAsync(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint, injectionPoint.getAnnotated());
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("To create a DocumentTemplateAsync from a ConfigurationUnit the database field is required");
        }
        ColumnFamilyManagerAsync manager = managerFactory.getAsync(database);
        return asyncProducer.get(manager);
    }
}
