/*
 *
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
 *
 */
package org.jnosql.diana.api;

import java.util.function.Supplier;

/**
 * This enum contains all the commons configurations that might be used to the NoSQL databases.
 * It implements {@link Supplier} which returns the property value on the arrangement.
 */
public enum  Configurations implements Supplier<String> {
    USER("jakarta.nosql.user"),
    PASSWORD("jakarta.nosql.password"),
    HOST("jakarta.nosql.host"),
    ENCRYPTION("jakarta.nosql.settings.encryption");

    private final String configuration;

    Configurations(String configuration) {
        this.configuration = configuration;
    }


    @Override
    public String get() {
        return configuration;
    }
}
