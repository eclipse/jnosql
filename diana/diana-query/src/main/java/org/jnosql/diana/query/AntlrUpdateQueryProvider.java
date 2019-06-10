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

import jakarta.nosql.query.JSONValue;
import jakarta.nosql.query.UpdateQuery;
import jakarta.nosql.query.UpdateQuery.UpdateQuerySupplier;
import org.antlr.v4.runtime.tree.ParseTree;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.Operator;

import jakarta.nosql.query.QueryValue;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * The {@link UpdateQuerySupplier} implementation that uses Antlr4
 */
public final class AntlrUpdateQueryProvider extends AbstractSupplier implements UpdateQuerySupplier {

    private String entity;

    private List<Condition> conditions = Collections.emptyList();

    private JSONValue value;

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::update;
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
        this.value = DefaultJSONValue.of(ctx);
    }

    private Condition getCondition(QueryParser.ChangeContext changeContext) {
        String name = changeContext.name().getText();
        Value<?> value = ValueConverter.get(changeContext.value());
        return new DefaultCondition(name, Operator.EQUALS, value);
    }


    @Override
    public UpdateQuery apply(String query) {
        runQuery(query);
        return new DefaultUpdateQuery(entity, conditions, value);
    }
}
