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
import org.eclipse.jnosql.query.grammar.QueryParser;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * A provider to {@link UpdateQuery}, this provider converts text into {@link UpdateQuery}
 */
public final class UpdateQueryConverter extends AbstractSupplier implements Function<String, UpdateQuery>  {

    private String entity;

    private List<DefaultQueryCondition> conditions = Collections.emptyList();

    private JSONQueryValue value;

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
        this.value = JSONQueryValue.of(ctx);
    }

    private DefaultQueryCondition getCondition(QueryParser.ChangeContext changeContext) {
        String name = changeContext.name().getText();
        QueryValue<?> queryValue = ValueConverter.get(changeContext.value());
        return new DefaultQueryCondition(name, Condition.EQUALS, queryValue);
    }


    @Override
    public UpdateQuery apply(String query) {
        runQuery(query);
        return new UpdateQuery(entity, conditions, value);
    }
}
