/*
 *  Copyright (c) 2019 Otávio Santana and others
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
 */
package org.jnosql.artemis.model;

public class Plate {

    private final int prefix;

    private final String sufix;


    private Plate(int prefix, String sufix) {
        this.prefix = prefix;
        this.sufix = sufix;
    }

    public int getPrefix() {
        return prefix;
    }

    public String getSufix() {
        return sufix;
    }

    @Override
    public String toString() {
        return prefix + '-' + sufix;
    }

    public static Plate of(String value) {
        String[] values = value.split("-");
        return new Plate(Integer.valueOf(values[0]), values[1]);
    }


}
