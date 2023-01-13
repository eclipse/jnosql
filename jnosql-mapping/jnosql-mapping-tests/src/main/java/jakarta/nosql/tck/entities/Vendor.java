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

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

import java.util.Collections;
import java.util.Set;

@Entity("vendors")
public class Vendor {

    @Id
    private String name;

    @Column
    private Set<String> prefixes;

    Vendor() {
    }

    public Vendor(String name) {
        this.name = name;
        prefixes = Collections.emptySet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(Set<String> prefixes) {
        this.prefixes = prefixes;
    }

    public void add(String prefix) {
        this.prefixes.add(prefix);
    }
}
