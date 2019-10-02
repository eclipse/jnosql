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
package org.eclipse.jnosql.artemis.configuration;


import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;

public class DocumentIntegrationAsync {

    private static final String DOCUMENT = "documentasync";

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentTemplateAsync template;

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentCollectionManagerAsync manager;

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentCollectionManagerAsyncFactory factory;

    public DocumentTemplateAsync getTemplate() {
        return template;
    }

    public DocumentCollectionManagerAsync getManager() {
        return manager;
    }

    public DocumentCollectionManagerAsyncFactory getFactory() {
        return factory;
    }
}
