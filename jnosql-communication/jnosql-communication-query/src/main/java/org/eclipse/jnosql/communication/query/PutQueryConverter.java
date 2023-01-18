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

import java.time.Duration;
import java.util.function.Function;

/**
 * A provider to {@link PutQuery}, this provider converts text into {@link PutQuery}
 */
public final class PutQueryConverter extends AbstractSupplier implements Function<String, PutQuery> {


    private QueryValue<?> key;

    private QueryValue<?> value;

    private Duration ttl;

    @Override
    public void exitKey(QueryParser.KeyContext ctx) {
        this.key = ValueConverter.get(ctx.value());
    }

    @Override
    public void exitValue(QueryParser.ValueContext ctx) {
        this.value = ValueConverter.get(ctx);
    }

    @Override
    public void exitTtl(QueryParser.TtlContext ctx) {
        this.ttl = Durations.get(ctx);
    }

    @Override
    Function<QueryParser, ParseTree> getParserTree() {
        return QueryParser::put;
    }

    @Override
    public PutQuery apply(String query) {
        runQuery(query);
        return new PutQuery(key, value, ttl);
    }

}
