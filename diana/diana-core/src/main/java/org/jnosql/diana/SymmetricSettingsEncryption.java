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

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * Symmetric-Key Cryptography is an encryption system in which the same key is used for the encoding and
 * decoding of the data. The safe distribution of the key is one of the drawbacks of this method,
 * but what it lacks in security it gains in time complexity.
 *The SettingsEncryption has two properties configurations.
 *
 * {@link SymmetricSettingsEncryption#PASSWORD_PROPERTY} The mandatory configuration that defines the password to both encrypt and decrypt the property.
 * {@link SymmetricSettingsEncryption#CRYPT_PROPERTY} This property defines the crypt algorithm that will use on the symmetric encryption process. The default value is DESede.
 * To know more about: https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html
 *
 */
public final class SymmetricSettingsEncryption implements SettingsEncryption {


    public static final String PASSWORD_PROPERTY = "jakarta.nosql.encryption.symmetric.password";
    public static final String CRYPT_PROPERTY = "jakarta.nosql.encryption.symmetric.crypt";

    private static final String MISSING_KEY_MESSAGE = "To use symmetric encryption you need to set the password using the property; " +
            PASSWORD_PROPERTY;
    private static final int MIN_VALUE = 24;
    private static final String ALGORITHM = "md5";
    private static final String CRYPT_DEFAULT_ALGORITHM = "DESede";



    @Override
    public String encrypt(String property, Settings settings) {
        checkArguments(property, settings);

        try {
            Cipher cipher = getCipher(settings, Cipher.ENCRYPT_MODE);

            byte[] plainTextBytes = property.getBytes(UTF_8);
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.getEncoder().encode(buf);
            return new String(base64Bytes, UTF_8);
        } catch (Exception ex) {
            throw new EncryptionException("An error when try to encrypt a property using symmetric", ex);
        }
    }

    @Override
    public String decrypt(String property, Settings settings) {
        checkArguments(property, settings);
        try {
            byte[] message = Base64.getDecoder().decode(property.getBytes(UTF_8));
            Cipher decipher = getCipher(settings, Cipher.DECRYPT_MODE);
            byte[] plainText = decipher.doFinal(message);
            return new String(plainText, UTF_8);
        } catch (Exception ex) {
            throw new EncryptionException("An error when try to decrypt a property using symmetric", ex);
        }
    }

    private Cipher getCipher(Settings settings, int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String password = getPassword(settings);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        byte[] digestOfPassword = md.digest(password.getBytes(UTF_8));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, MIN_VALUE);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }
        String crypt = SettingsPriority.get(CRYPT_PROPERTY, settings)
                .map(Object::toString)
                .orElse(CRYPT_DEFAULT_ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(keyBytes, crypt);
        Cipher cipher = Cipher.getInstance(crypt);
        cipher.init(mode, secretKey);
        return cipher;
    }

    private String getPassword(Settings settings) {
        return SettingsPriority.get(PASSWORD_PROPERTY, settings)
                .map(Object::toString)
                .orElseThrow(() -> new JNoSQLException(MISSING_KEY_MESSAGE));
    }

    private void checkArguments(String property, Settings settings) {
        requireNonNull(property, "property is required");
        requireNonNull(settings, "settings is required");
    }
}
