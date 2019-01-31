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
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.DeleteQuerySupplier;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * The {@link DeleteQuerySupplier} implementation that uses Antlr4
 */
public final class AntlrDeleteQuerySupplier extends AbstractWhereSupplier implements DeleteQuerySupplier {

    private String entity;

    private List<String> fields = emptyList();


    @Override
    public void exitDeleteFields(QueryParser.DeleteFieldsContext ctx) {
        this.fields = ctx.name().stream().map(QueryParser.NameContext::getText).collect(toList());
    }


    @Override
    public void exitEntity(QueryParser.EntityContext ctx) {
        this.entity = ctx.getText();
    }

    @Override
    public DeleteQuery apply(String query) {
        runQuery(query);
        return new DefaultDeleteQuery(entity, fields, where);
    }

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::delete;
    }
}