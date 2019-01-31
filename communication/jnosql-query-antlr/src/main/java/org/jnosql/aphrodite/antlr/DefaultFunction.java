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

import org.jnosql.query.Function;

import java.util.Arrays;
import java.util.Objects;

final class DefaultFunction implements Function {

    private final String name;

    private final Object[] args;

    private DefaultFunction(String name, Object[] args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object[] getParams() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultFunction)) {
            return false;
        }
        DefaultFunction that = (DefaultFunction) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(name) + Arrays.hashCode(args);
    }

    @Override
    public String toString() {
        return name + "(" + Arrays.toString(args) + ")";
    }

    public static Function of(String name, Object[] args) {
        return new DefaultFunction(name, args);
    }
}
