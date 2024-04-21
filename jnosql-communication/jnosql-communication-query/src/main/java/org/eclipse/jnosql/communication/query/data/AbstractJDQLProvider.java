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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryErrorListener;
import org.eclipse.jnosql.communication.query.Where;
import org.eclipse.jnosql.query.grammar.data.JDQLBaseListener;
import org.eclipse.jnosql.query.grammar.data.JDQLLexer;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.Objects;
import java.util.function.Function;

abstract class AbstractJDQLProvider extends JDQLBaseListener {

    protected Where where;

    protected QueryCondition condition;

    protected boolean and = true;

    protected String entity;

    protected void runQuery(String query) {

        CharStream stream = CharStreams.fromString(query);
        JDQLLexer lexer = new JDQLLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JDQLParser parser = new JDQLParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(QueryErrorListener.INSTANCE);
        parser.addErrorListener(QueryErrorListener.INSTANCE);

        ParseTree tree = parser.select_statement();
        ParseTreeWalker.DEFAULT.walk(this, tree);

        if (Objects.nonNull(condition)) {
            this.where = Where.of(condition);
        }
    }

    @Override
    public void exitFrom_clause(JDQLParser.From_clauseContext ctx) {
        this.entity = ctx.entity_name().getText();
    }

    abstract Function<JDQLParser, ParseTree> getParserTree();
}
