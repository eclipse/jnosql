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
import org.jnosql.diana.api.SettingsPriority;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
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

    private static final String CRYPT_DEFAULT_ALGORITHM = "RSA";

    private static final String ERROR_MESSAGE_PRIVATE = "The private key property is mandatory with either resource or" +
            " absolute path setting the property: " + PRIVATE_PROPERTY;

    private static final String ERROR_MESSAGE_PUBLIC = "The public key property is mandatory with either resource or" +
            " absolute path setting the property: " + PUBLIC_PROPERTY;

    @Override
    public String encrypt(String property, Settings settings) {
        checkArguments(property, settings);
        String file = SettingsPriority.get(PRIVATE_PROPERTY, settings)
                .map(Object::toString)
                .orElseThrow(() -> new EncryptionException(ERROR_MESSAGE_PRIVATE));

        try {
            byte[] contents = contents(file);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(contents);
            KeyFactory keyFactory = getKeyFactory(settings);
            PrivateKey privateKey = keyFactory.generatePrivate(spec);
            Cipher cipher = getCipher(settings);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] plainTextBytes = property.getBytes(UTF_8);
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.getEncoder().encode(buf);
            return new String(base64Bytes, UTF_8);
        } catch (InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException exp) {
            throw new EncryptionException("Error to encrypt at asymmetric ", exp);
        }
    }

    @Override
    public String decrypt(String property, Settings settings) {
        checkArguments(property, settings);
        String file = SettingsPriority.get(PUBLIC_PROPERTY, settings)
                .map(Object::toString)
                .orElseThrow(() -> new EncryptionException(ERROR_MESSAGE_PUBLIC));

        try {
            byte[] contents = contents(file);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(contents);
            KeyFactory keyFactory = getKeyFactory(settings);
            PublicKey publicKey = keyFactory.generatePublic(spec);
            Cipher cipher = getCipher(settings);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] message = Base64.getDecoder().decode(property.getBytes(UTF_8));
            byte[] plainText = cipher.doFinal(message);
            return new String(plainText, UTF_8);
        }catch (InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException exp) {
            throw new EncryptionException("Error to decrypt at asymmetric ", exp);
        }
    }

    private void checkArguments(String property, Settings settings) {
        requireNonNull(property, "property is required");
        requireNonNull(settings, "settings is required");
    }

    private byte[] contents(String file) {
        try {
            URL url = AsymmetricSettingsEncryption.class.getClassLoader().getResource(file);
            if (url != null) {
                return Files.readAllBytes(Paths.get(url.toURI()));
            }
            Path path = Paths.get(file);
            if (Files.exists(path)) {
                Files.readAllBytes(path);
            }
        } catch (IOException | URISyntaxException ex) {
            throw new EncryptionException("Error to read the file " + file, ex);
        }
        throw new EncryptionException("Path does not exist " + file);
    }

    private Cipher getCipher(Settings settings) {
        String cipher = SettingsPriority
                .get(CRYPT_PROPERTY, settings)
                .map(Object::toString)
                .orElse(CRYPT_DEFAULT_ALGORITHM);
        try {
            return Cipher.getInstance(cipher);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException exp) {
            throw new EncryptionException("Error to load Cipher at asymmetric", exp);
        }
    }

    private KeyFactory getKeyFactory(Settings settings) {
        String cipher = SettingsPriority
                .get(CRYPT_PROPERTY, settings)
                .map(Object::toString)
                .orElse(CRYPT_DEFAULT_ALGORITHM);
        try {
            return KeyFactory.getInstance(cipher);
        } catch (NoSuchAlgorithmException exp) {
            throw new EncryptionException("Error to load KeyFactory at asymmetric", exp);
        }
    }
}
