/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.diana.query;

import jakarta.nosql.query.Condition;
import jakarta.nosql.query.InsertQuery;
import jakarta.nosql.query.InsertQuery.InsertQueryProvider;
import jakarta.nosql.query.JSONQueryValue;
import jakarta.nosql.query.Operator;
import jakarta.nosql.query.QueryValue;
import org.antlr.v4.runtime.tree.ParseTree;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * The {@link InsertQueryProvider} implementation that uses Antlr4
 */
public final class AntlrInsertQueryProvider extends AbstractSupplier implements InsertQueryProvider {

    private String entity;

    private List<Condition> conditions = Collections.emptyList();

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
        this.value = DefaultJSONQueryValue.of(ctx);
    }

    private Condition getCondition(QueryParser.ChangeContext changeContext) {
        String name = changeContext.name().getText();
        QueryValue<?> queryValue = ValueConverter.get(changeContext.value());
        return new DefaultCondition(name, Operator.EQUALS, queryValue);
    }

    @Override
    public void exitTtl(QueryParser.TtlContext ctx) {
        this.duration = Durations.get(ctx);
    }


    @Override
    public InsertQuery apply(String query) {
        runQuery(query);
        return new DefaultInsertQuery(entity, duration, conditions, value);
    }
}
