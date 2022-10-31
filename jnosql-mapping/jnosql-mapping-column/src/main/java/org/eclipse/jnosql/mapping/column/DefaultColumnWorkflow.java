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
package org.eclipse.jnosql.mapping.column;


import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class DefaultColumnWorkflow extends AbstractColumnWorkflow{


    private ColumnEventPersistManager columnEventPersistManager;


    private ColumnEntityConverter converter;

    DefaultColumnWorkflow() {
    }

    @Inject
    DefaultColumnWorkflow(ColumnEventPersistManager columnEventPersistManager, ColumnEntityConverter converter) {
        this.columnEventPersistManager = columnEventPersistManager;
        this.converter = converter;
    }

    @Override
    protected ColumnEventPersistManager getColumnEventPersistManager() {
        return columnEventPersistManager;
    }

    @Override
    protected ColumnEntityConverter getConverter() {
        return converter;
    }
}
