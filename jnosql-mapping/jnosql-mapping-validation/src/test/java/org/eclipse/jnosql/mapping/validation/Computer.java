/*
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
 */
package org.eclipse.jnosql.mapping.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Computer {

    private final String name;

    private final int version;

    private final String model;

    public Computer(@NotNull String name, @Min(2020) int version,
                    @NotNull @Size(min = 2, max = 4) String model) {
        this.name = name;
        this.version = version;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getModel() {
        return model;
    }
}
