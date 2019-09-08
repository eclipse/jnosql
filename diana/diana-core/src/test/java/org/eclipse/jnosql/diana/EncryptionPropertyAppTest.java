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
package org.eclipse.jnosql.diana;

import jakarta.nosql.Configurations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptionPropertyAppTest {


    @Test
    public void shouldReturnErrorWhenThereIsNotParameter() {
        assertThrows(IllegalArgumentException.class, () -> EncryptionPropertyApp.main(new String[0]));
    }

    @Test
    public void shouldReturnErrorWhenParameterIsNull() {
        assertThrows(IllegalArgumentException.class, () -> EncryptionPropertyApp.main(new String[]{null}));
    }

    @Test
    public void shouldEncrypt() {
        System.setProperty(SymmetricSettingsEncryption.PASSWORD_PROPERTY, "password");
        System.setProperty(Configurations.ENCRYPTION.get(), "symmetric");

        EncryptionPropertyApp.main(new String[]{"Ada Lovelace"});

        System.clearProperty(SymmetricSettingsEncryption.PASSWORD_PROPERTY);
        System.clearProperty(Configurations.ENCRYPTION.get());
    }
}