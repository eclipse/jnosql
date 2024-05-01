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

import org.eclipse.jnosql.communication.query.NumberQueryValue;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.StringQueryValue;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.function.Function;

enum PrimaryFunction implements Function<JDQLParser.Primary_expressionContext, QueryValue<?>> {

    INSTANCE;

    @Override
    public QueryValue<?> apply(JDQLParser.Primary_expressionContext context) {
        if(context.literal() != null) {
            var literal = context.literal();
            if(literal.STRING() != null){
                String text = literal.STRING().getText();
                return StringQueryValue.of(text.substring(1, text.length() - 1));
            } else if(literal.INTEGER() != null) {
                return NumberQueryValue.of(Integer.valueOf(literal.INTEGER().getText()));
            } else if(literal.DOUBLE() != null) {
                return NumberQueryValue.of(Double.valueOf(literal.DOUBLE().getText()));
            }
        } else if(context.input_parameter() != null) {
            return DefaultQueryValue.of(context.input_parameter().getText());
        }
        return null;
    }
}