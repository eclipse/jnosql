/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.data;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.jnosql.communication.query.UpdateQuery;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.Objects;
import java.util.function.Function;

public final class UpdateProvider extends AbstractWhere implements Function<String, UpdateQuery> {

    @Override
    public UpdateQuery apply(String query) {
        Objects.requireNonNull(query, " query is required");
        return null;
    }

    @Override
    ParserRuleContext getTree(JDQLParser parser) {
        return parser.update_statement();
    }
}
