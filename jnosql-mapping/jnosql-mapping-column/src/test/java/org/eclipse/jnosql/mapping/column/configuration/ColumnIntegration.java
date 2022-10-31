/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.column.configuration;


import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;

public class ColumnIntegration {

    private static final String COLUMN = "column";

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnTemplate template;

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnFamilyManager manager;

    @Inject
    @ConfigProperty(name = COLUMN)
    private ColumnFamilyManagerFactory factory;

    public ColumnTemplate getTemplate() {
        return template;
    }

    public ColumnFamilyManager getManager() {
        return manager;
    }

    public ColumnFamilyManagerFactory getFactory() {
        return factory;
    }
}
