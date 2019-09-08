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
package org.eclipse.jnosql.artemis.document.spi;

import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import jakarta.nosql.mapping.document.DocumentTemplateAsyncProducer;
import jakarta.nosql.mapping.document.DocumentTemplateProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * It creates both a {@link DocumentTemplate} and a {@link DocumentTemplateAsync} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class TemplateConfigurationProducer {

    @Inject
    private DocumentTemplateProducer producer;

    @Inject
    private DocumentTemplateAsyncProducer asyncProducer;

    @Inject
    private ManagerConfigurationProducer managerProducer;


    @ConfigurationUnit
    @Produces
    public DocumentTemplate get(InjectionPoint injectionPoint) {
        return producer.get(managerProducer.get(injectionPoint));
    }

    @ConfigurationUnit
    @Produces
    public DocumentTemplateAsync getAsync(InjectionPoint injectionPoint) {
        return asyncProducer.get(managerProducer.getAsync(injectionPoint));
    }

}
