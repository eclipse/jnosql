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
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.Where;
import org.eclipse.jnosql.query.grammar.data.JDQLLexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JakartaDataQueryProviderTest {


    private SelectJDQL selectProvider;

    @BeforeEach
    void setUp() {
        selectProvider = new SelectJDQL();
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"FROM entity"})
    void shouldReturnParserQuery(String query) {
        CharStream stream = CharStreams.fromString(query);
        JDQLLexer lexer = new JDQLLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        tokens.fill();
        for (Token token : tokens.getTokens()) {
            System.out.println(token.getText() + " : " + token.getType());
        }
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"FROM entity"})
    void shouldReturnParserQuery2(String query) {
        SelectJDQL selectJDQL = new SelectJDQL();
        selectJDQL.apply(query, "entity");
    }


}
