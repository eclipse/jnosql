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

import org.jnosql.query.RemoveQuery;
import org.jnosql.query.Value;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

final class DefaultRemoveQuery implements RemoveQuery {

    private final List<Value<?>> keys;

    DefaultRemoveQuery(List<Value<?>> keys) {
        this.keys = keys;
    }

    @Override
    public List<Value<?>> getKeys() {
        return unmodifiableList(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultRemoveQuery)) {
            return false;
        }
        DefaultRemoveQuery that = (DefaultRemoveQuery) o;
        return Objects.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(keys);
    }

    @Override
    public String toString() {
        return this.keys.toString();
    }
}
