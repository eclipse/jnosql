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

import java.util.Collections;

/**
 * An executable class that given property, that will be the first parameter, it will return print the property encrypted.
 * To run it:
 *
 * <p>{@code java -cp diana-core-VERSION.jar -Djakarta.nosql.settings.encryption="symmetric" -Djakarta.nosql.encryption.symmetric.password="password"
 * EncryptionPropertyApp "sensible data"}</p>
 */
public final class EncryptionPropertyApp {

    public static void main(String[] args) {
        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("A non null parameter is required");
        }
        String property = args[0];
        SettingsEncryption encryption = SettingsEncryption.get();
        String encrypted = encryption.encrypt(property, Settings.of(Collections.emptyMap()));
        System.out.println("ENC(" + encrypted + ')');

    }

    private EncryptionPropertyApp() {
    }
}
