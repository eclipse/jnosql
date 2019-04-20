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

import static java.util.Objects.requireNonNull;

/**
 * Public-key cryptography, or asymmetric cryptography, is a cryptographic system that uses pairs of keys:
 * public keys which may be disseminated widely, and private keys which are known only to the owner.
 * The generation of such keys depends on cryptographic algorithms based on mathematical problems to produce one-way
 * functions. Effective security only requires keeping the private key private;
 * the public key can be openly distributed without compromising security.
 *
 * <p>{@link AsymmetricSettingsEncryption#PRIVATE_PROPERTY} Either the resource URL or absolute path of a public key, to know more:
 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html</p>
 * <p>{@link AsymmetricSettingsEncryption#PUBLIC_PROPERTY} Either the resource URL or absolute path of a private key, to know more:
 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html</p>
 * <p>{@link AsymmetricSettingsEncryption#CRYPT_PROPERTY} This property defines the crypt algorithm that will use on the symmetric encryption process. The default value is <b>RSA</b>
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html.
 * </p>
 */
public class AsymmetricSettingsEncryption implements SettingsEncryption {

    public static final String PRIVATE_PROPERTY = "jakarta.nosql.encryption.asymmetric.private";
    public static final String PUBLIC_PROPERTY = "jakarta.nosql.encryption.asymmetric.public";
    public static final String CRYPT_PROPERTY = "jakarta.nosql.encryption.asymmetric.crypt";

    private static final String CRYPT_DEFAULT_ALGORITHM = "DESede";

    @Override
    public String encrypt(String property, Settings settings) {
        checkArguments(property, settings);
        return null;
    }

    @Override
    public String decrypt(String property, Settings settings) {
        checkArguments(property, settings);
        return null;
    }

    private void checkArguments(String property, Settings settings) {
        requireNonNull(property, "property is required");
        requireNonNull(settings, "settings is required");
    }
}
