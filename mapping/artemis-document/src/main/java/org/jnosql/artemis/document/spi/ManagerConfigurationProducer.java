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
package org.jnosql.artemis.document.spi;

import org.jnosql.artemis.ConfigurationException;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.document.DocumentTemplateAsyncProducer;
import org.jnosql.artemis.document.DocumentTemplateProducer;
import org.jnosql.artemis.util.StringUtils;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsyncFactory;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * It creates both a {@link DocumentCollectionManager} and a {@link DocumentCollectionManagerAsync} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class ManagerConfigurationProducer {

    @Inject
    private DocumentCollectionConfigurationProducer configurationProducer;

    @Inject
    private DocumentTemplateProducer producer;

    @Inject
    private DocumentTemplateAsyncProducer asyncProducer;


    @ConfigurationUnit
    @Produces
    public DocumentCollectionManager get(InjectionPoint injectionPoint) {
        DocumentCollectionManagerFactory<?> managerFactory = configurationProducer.getDocumentConfiguration(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint, injectionPoint.getAnnotated());
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("To create a DocumentTemplate from a ConfigurationUnit the database field is required");
        }
        return managerFactory.get(database);
    }

    @ConfigurationUnit
    @Produces
    public DocumentCollectionManagerAsync getAsync(InjectionPoint injectionPoint) {
        DocumentCollectionManagerAsyncFactory<?> managerFactory = configurationProducer.getDocumentManagerAsync(injectionPoint);
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint, injectionPoint.getAnnotated());
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("To create a DocumentTemplateAsync from a ConfigurationUnit the database field is required");
        }
        return managerFactory.getAsync(database);
    }

}
