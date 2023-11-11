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

import java.util.Arrays;
import java.util.Objects;

record DefaultFunction(String name, Object[] params) implements Function {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultFunction that)) {
            return false;
        }
        return Objects.equals(name, that.name) &&
                Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(name) + Arrays.hashCode(params);
    }

    @Override
    public String toString() {
        return name + "(" + Arrays.toString(params) + ")";
    }

    static Function of(String name, Object[] args) {
        return new DefaultFunction(name, args);
    }
}
