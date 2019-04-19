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
package org.jnosql.diana.api.encryption;

import org.jnosql.diana.api.Settings;
import org.jnosql.diana.api.SettingsEncryption;

/**
 * In cryptography, Triple DES (3DES or TDES), officially the Triple Data Encryption Algorithm (TDEA or Triple DEA),
 * is a symmetric-key block cipher, which applies the DES cipher algorithm three times to each data block.
 */
public class TripleDESSettingsEncryption implements SettingsEncryption {

    @Override
    public String encrypt(String property, Settings settings) {
        return null;
    }

    @Override
    public String decrypt(String property, Settings settings) {
        return null;
    }
}
