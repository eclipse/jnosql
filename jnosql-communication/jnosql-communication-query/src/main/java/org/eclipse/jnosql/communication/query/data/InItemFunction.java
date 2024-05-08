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

import org.eclipse.jnosql.communication.query.EnumQueryValue;
import org.eclipse.jnosql.communication.query.NumberQueryValue;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.StringQueryValue;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.function.Function;

enum InItemFunction implements Function<JDQLParser.In_itemContext, QueryValue<?>> {

    INSTANCE;


    @Override
    public QueryValue<?> apply(JDQLParser.In_itemContext item) {
        if(item.literal() != null) {
            var literal = item.literal();
            if(literal.STRING()!= null){
                String text = literal.STRING().getText();
                return StringQueryValue.of(text.substring(1, text.length() - 1));
            } else if(literal.DOUBLE() != null){
                return NumberQueryValue.of(Double.valueOf(literal.DOUBLE().getText()));
            } else if(literal.INTEGER() != null){
                return NumberQueryValue.of(Integer.valueOf(literal.INTEGER().getText()));
            }
        } else if(item.enum_literal() != null){
            Enum<?> value = EnumConverter.INSTANCE.apply(item.enum_literal().getText());
            return EnumQueryValue.of(value);
        } else if(item.input_parameter() != null){
            return DefaultQueryValue.of(item.input_parameter().getText());
        }
        throw new UnsupportedOperationException("The in item is not supported yet: " + item.getText());
    }
}
