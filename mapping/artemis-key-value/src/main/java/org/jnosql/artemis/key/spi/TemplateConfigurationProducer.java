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
package org.jnosql.artemis.key.spi;

import org.jnosql.artemis.ConfigurationException;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.key.KeyValueTemplate;
import org.jnosql.artemis.key.KeyValueTemplateProducer;
import org.jnosql.artemis.util.StringUtils;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.BucketManagerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * It creates a {@link KeyValueTemplate} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class TemplateConfigurationProducer {

    @Inject
    private KeyValueConfigurationProducer configurationProducer;

    @Inject
    private KeyValueTemplateProducer producer;

    @ConfigurationUnit
    @Produces
    public KeyValueTemplate getKeyValueTemplate(InjectionPoint injectionPoint) {
        BucketManagerFactory bucketManagerFactory = configurationProducer.getBucketManager(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint, injectionPoint.getAnnotated());
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("To create a KeyValueTemplate from a ConfigurationUnit the database field is required");
        }
        BucketManager bucketManager = bucketManagerFactory.getBucketManager(database);
        return producer.get(bucketManager);
    }

}
