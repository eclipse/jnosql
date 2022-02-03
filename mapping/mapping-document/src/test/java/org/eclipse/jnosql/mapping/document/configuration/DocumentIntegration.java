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
package org.eclipse.jnosql.mapping.document.configuration;


import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;

public class DocumentIntegration {

    private static final String DOCUMENT = "document";

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentTemplate template;

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentCollectionManager manager;

    @Inject
    @ConfigProperty(name = DOCUMENT)
    private DocumentCollectionManagerFactory factory;

    public DocumentTemplate getTemplate() {
        return template;
    }

    public DocumentCollectionManager getManager() {
        return manager;
    }

    public DocumentCollectionManagerFactory getFactory() {
        return factory;
    }
}
