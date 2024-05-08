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
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.jnosql.communication.query.QueryErrorListener;
import org.eclipse.jnosql.query.grammar.data.JDQLBaseListener;
import org.eclipse.jnosql.query.grammar.data.JDQLLexer;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;


abstract class AbstractJDQLProvider extends JDQLBaseListener {

    protected void runQuery(String query) {

        CharStream stream = CharStreams.fromString(query);
        JDQLLexer lexer = new JDQLLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JDQLParser parser = new JDQLParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(QueryErrorListener.INSTANCE);
        parser.addErrorListener(QueryErrorListener.INSTANCE);

        var tree = getTree(parser);
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    abstract ParserRuleContext getTree(JDQLParser parser);
}
