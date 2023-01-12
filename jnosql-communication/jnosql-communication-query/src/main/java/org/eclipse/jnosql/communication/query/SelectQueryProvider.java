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
import org.eclipse.jnosql.communication.Sort;
import org.eclipse.jnosql.communication.SortType;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * A provider to {@link DefaultSelectQuery}, this provider converts text into {@link DefaultSelectQuery}
 */
public final class SelectQueryProvider extends AbstractWhereSupplier implements Function<String, DefaultSelectQuery> {

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
        this.skip = Long.parseLong(ctx.INT().getText());
    }

    @Override
    public void exitLimit(QueryParser.LimitContext ctx) {
        this.limit = Long.parseLong(ctx.INT().getText());
    }

    @Override
    public void exitEntity(QueryParser.EntityContext ctx) {
        this.entity = ctx.getText();
    }

    @Override
    public void enterOrder(QueryParser.OrderContext ctx) {
        this.sorts = ctx.orderName().stream().map(this::sort).collect(Collectors.toList());
    }


    @Override
    public DefaultSelectQuery apply(String query) {
        runQuery(query);
        return new DefaultSelectQuery(entity, fields, sorts, skip, limit, where);
    }

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::select;
    }

    private Sort sort(QueryParser.OrderNameContext context) {
        String text = context.name().getText();
        SortType type = context.desc() == null? SortType.ASC: SortType.DESC;
        return Sort.of(text, type);
    }
}