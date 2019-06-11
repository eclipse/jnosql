/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.column.query;

import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnQueryParams;
import jakarta.nosql.column.SelectQueryConverter;
import jakarta.nosql.query.SelectQuery;

enum SelectQueryConverterFactory implements SelectQueryConverter {

    INSTANCE;

    private final SelectQueryParser parser = new SelectQueryParser();

    @Override
    public ColumnQueryParams apply(SelectQuery selectQuery, ColumnObserverParser columnObserverParser) {
        return parser.apply(selectQuery, columnObserverParser);
    }
}
