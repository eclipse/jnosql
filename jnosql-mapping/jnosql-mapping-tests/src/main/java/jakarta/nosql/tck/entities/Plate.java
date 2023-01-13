/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities;

import java.util.Objects;

public final class Plate {

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
        return Integer.toString(prefix) + '-' + sufix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plate plate = (Plate) o;
        return prefix == plate.prefix &&
                Objects.equals(sufix, plate.sufix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, sufix);
    }

    public static Plate of(String value) {
        String[] values = value.split("-");
        return new Plate(Integer.valueOf(values[0]), values[1]);
    }


}
