/*
 *
 *  Copyright (c) 2019 Otavio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana;

import jakarta.nosql.Params;
import jakarta.nosql.Value;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

final class DefaultParams implements Params {


    private final List<ParamValue> parameters = new ArrayList<>();

    @Override
    public boolean isNotEmpty() {
        return !parameters.isEmpty();
    }

    @Override
    public Value add(String param) {
        ParamValue value = new ParamValue(param);
        parameters.add(value);
        return value;
    }

    @Override
    public List<String> getParametersNames() {
        return parameters.stream().map(ParamValue::getName).collect(toList());
    }

    @Override
    public String toString() {
        return parameters.stream().map(ParamValue::getName).collect(joining(","));
    }

    @Override
    public void bind(String name, Object value) {
        parameters.stream().filter(p -> p.getName().equals(name)).forEach(p -> p.setValue(value));
    }
}
