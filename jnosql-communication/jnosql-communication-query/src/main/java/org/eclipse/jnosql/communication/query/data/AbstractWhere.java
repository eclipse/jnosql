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

import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.Where;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.Objects;

abstract class AbstractWhere extends AbstractJDQLProvider {

    protected Where where;

    protected QueryCondition condition;

    protected boolean and = true;

    protected String entity;

    @Override
    protected void runQuery(String query) {
        super.runQuery(query);

        if (Objects.nonNull(condition)) {
            this.where = Where.of(condition);
        }
    }

    @Override
    public void exitFrom_clause(JDQLParser.From_clauseContext ctx) {
        this.entity = ctx.entity_name().getText();
    }
}
