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
package org.jnosql.diana.api.key.query;

import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValueEntity;
import org.jnosql.diana.api.key.KeyValuePreparedStatement;
import org.jnosql.diana.api.key.KeyValueQueryParser;

import java.util.List;

public class DefaultKeyValueQueryParser implements KeyValueQueryParser {

    @Override
    public List<KeyValueEntity<?>> query(String query, BucketManager manager) {
        return null;
    }

    @Override
    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        return null;
    }
}
