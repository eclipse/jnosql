/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jnosql.communication.Condition;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * A provider to {@link InsertQuery}, this provider converts text into {@link InsertQuery}
 */
public final class InsertQueryProvider extends AbstractSupplier implements Function<String, InsertQuery> {

    private String entity;

    private List<QueryCondition> conditions = Collections.emptyList();

    private Duration duration;

    private JSONQueryValue value;

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::insert;
    }

    @Override
    public void exitEntity(QueryParser.EntityContext ctx) {
        this.entity = ctx.getText();
    }

    @Override
    public void exitChanges(QueryParser.ChangesContext ctx) {
        this.conditions = ctx.change().stream().map(this::getCondition).collect(toList());
    }

    @Override
    public void enterJson(QueryParser.JsonContext ctx) {
        this.value = JSONQueryValue.of(ctx);
    }

    private QueryCondition getCondition(QueryParser.ChangeContext changeContext) {
        String name = changeContext.name().getText();
        QueryValue<?> queryValue = ValueConverter.get(changeContext.value());
        return new QueryCondition(name, Condition.EQUALS, queryValue);
    }

    @Override
    public void exitTtl(QueryParser.TtlContext ctx) {
        this.duration = Durations.get(ctx);
    }


    @Override
    public InsertQuery apply(String query) {
        runQuery(query);
        return new InsertQuery(entity, duration, conditions, value);
    }
}
