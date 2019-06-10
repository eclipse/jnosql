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
import org.jnosql.query.GetQuery;
import org.jnosql.query.GetQuerySupplier;
import org.jnosql.query.Value;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * The {@link GetQuerySupplier} implementation that uses Antlr4
 */
public final class AntlrGetQuerySupplier extends AbstractSupplier implements GetQuerySupplier {

    private List<Value<?>> keys = Collections.emptyList();

    @Override
    public void exitKeys(QueryParser.KeysContext ctx) {
        this.keys =  ctx.value().stream().map(ValueConverter::get).collect(toList());
    }

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::get;
    }

    @Override
    public GetQuery apply(String query) {
        runQuery(query);
        return new DefaultGetQuery(keys);
    }

}
