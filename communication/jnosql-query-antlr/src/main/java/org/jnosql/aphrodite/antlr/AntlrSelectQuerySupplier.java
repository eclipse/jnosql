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

package org.jnosql.aphrodite.antlr;

import org.antlr.v4.runtime.tree.ParseTree;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;
import org.jnosql.query.Sort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * The {@link SelectQuerySupplier} implementation that uses Antlr4
 */
public final class AntlrSelectQuerySupplier extends AbstractWhereSupplier implements SelectQuerySupplier {

    private String entity;

    private List<String> fields = emptyList();

    private List<Sort> sorts = emptyList();

    private long skip;

    private long limit;


    @Override
    public void exitFields(QueryParser.FieldsContext ctx) {
        this.fields = ctx.name().stream().map(QueryParser.NameContext::getText).collect(toList());
    }

    @Override
    public void exitSkip(QueryParser.SkipContext ctx) {
        this.skip = Long.valueOf(ctx.INT().getText());
    }

    @Override
    public void exitLimit(QueryParser.LimitContext ctx) {
        this.limit = Long.valueOf(ctx.INT().getText());
    }

    @Override
    public void exitEntity(QueryParser.EntityContext ctx) {
        this.entity = ctx.getText();
    }

    @Override
    public void enterOrder(QueryParser.OrderContext ctx) {
        this.sorts = ctx.orderName().stream().map(DefaultSort::of).collect(Collectors.toList());
    }


    @Override
    public SelectQuery apply(String query) {
        runQuery(query);
        return new DefaultSelectQuery(entity, fields, sorts, skip, limit, where);
    }

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::select;
    }
}