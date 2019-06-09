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
package org.jnosql.diana;

import java.util.function.Supplier;

/**
 * This enum contains all the commons configurations that might be used to the NoSQL databases.
 * It implements {@link Supplier} which returns the property value on the arrangement.
 */
public enum Configurations implements Supplier<String> {
    /**
     * to set a user in a NoSQL database
     */
    USER("jakarta.nosql.user"),
    /**
     * to set a password in a database
     */
    PASSWORD("jakarta.nosql.password"),
    /**
     * the host configuration that might have more than one with a number as a suffix,
     * such as jakarta.nosql.host-1=localhost, jakarta.nosql.host-2=host2
     */
    HOST("jakarta.nosql.host"),
    /**
     * A configuration to set the encryption to settings property.
     */
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
