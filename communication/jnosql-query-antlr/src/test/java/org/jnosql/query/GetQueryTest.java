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

package org.jnosql.query;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jnosql.aphrodite.antlr.QueryBaseListener;
import org.jnosql.aphrodite.antlr.QueryErrorListener;
import org.jnosql.aphrodite.antlr.QueryLexer;
import org.jnosql.aphrodite.antlr.QueryParser;
import org.jnosql.aphrodite.provider.GetQueryArgumentProvider;
import org.jnosql.aphrodite.provider.WrongGetQueryArgumentProvider;
import org.jnosql.query.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class GetQueryTest {

    @ParameterizedTest
    @ArgumentsSource(GetQueryArgumentProvider.class)
    public void shouldExecuteQuery(String query) {
        testQuery(query);
    }

    @Test
    public void shouldIgnoreComments() {
        testQuery("//ignore this line \n get 12");
    }

    @ParameterizedTest
    @ArgumentsSource(WrongGetQueryArgumentProvider.class)
    public void shouldNotExecute(String query) {
        Assertions.assertThrows(QueryException.class, () -> testQuery(query));
    }

    private void testQuery(String query) {
        CharStream stream = CharStreams.fromString(query);
        QueryLexer lexer = new QueryLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(QueryErrorListener.INSTANCE);
        parser.addErrorListener(QueryErrorListener.INSTANCE);

        ParseTree tree = parser.get();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new QueryBaseListener(), tree);


    }


}
