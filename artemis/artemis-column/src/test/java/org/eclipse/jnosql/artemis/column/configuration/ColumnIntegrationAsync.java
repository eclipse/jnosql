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
package org.eclipse.jnosql.artemis.column.configuration;


import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;

public class ColumnIntegrationAsync {

    private static final String COLUMN = "columnasync";

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnTemplateAsync template;

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnFamilyManagerAsync manager;

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnFamilyManagerAsyncFactory factory;

    public ColumnTemplateAsync getTemplate() {
        return template;
    }

    public ColumnFamilyManagerAsync getManager() {
        return manager;
    }

    public ColumnFamilyManagerAsyncFactory getFactory() {
        return factory;
    }
}
