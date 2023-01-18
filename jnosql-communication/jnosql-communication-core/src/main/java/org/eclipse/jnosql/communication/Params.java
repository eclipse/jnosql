/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication;


import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


/**
 * A group of params to a dynamic query
 */
public final class Params {

    private final List<ParamValue> parameters = new ArrayList<>();


    /**
     * @return if the params list is not empty
     */
    public boolean isNotEmpty() {
        return !parameters.isEmpty();
    }

    /**
     * Adds a new value at the params
     *
     * @param param the param
     * @return the {@link Value}
     */
    public Value add(String param) {
        ParamValue value = new ParamValue(param);
        parameters.add(value);
        return value;
    }

    /**
     * set the value from the class
     *
     * @param name  the name
     * @param value the value
     */
    public void bind(String name, Object value) {
        parameters.stream().filter(p -> p.getName().equals(name)).forEach(p -> p.setValue(value));
    }

    /**
     * @return the parameters name at the params
     */
    public List<String> getParametersNames() {
        return parameters.stream().map(ParamValue::getName).collect(toList());
    }

    @Override
    public String toString() {
        return parameters.stream().map(ParamValue::getName).collect(joining(","));
    }



    /**
     * It returns a new Params instance
     *
     * @return a new {@link Params} instance
     */
    public static Params newParams() {
        return new Params();
    }

}
