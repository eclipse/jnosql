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
package org.jnosql.aphrodite.antlr.method;

import org.jnosql.query.Sort;

import java.util.Locale;
import java.util.Objects;

final class MethodSort implements Sort {

    private final String name;

    private final SortType type;

    private MethodSort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SortType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodSort)) {
            return false;
        }
        MethodSort that = (MethodSort) o;
        return Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + " " + type.name();
    }

    public static Sort of(MethodParser.OrderNameContext context) {
        String text = context.variable().getText();
        String lowerCase = String.valueOf(text.charAt(0)).toLowerCase(Locale.US);
        SortType type = context.desc() == null? SortType.ASC: SortType.DESC;
        return new MethodSort(lowerCase.concat(text.substring(1)), type);
    }
}