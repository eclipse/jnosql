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

/**
 * This instance will encrypt and decrypt properties of {@link Settings}.
 * To set the encryption, put the value on the property: <b>jakarta.nosql.settings.encryption</b>.
 * The value is the {@link Class#getSimpleName()} from implementation and might define from {@link System#getProperties()}, {@link System#getenv()} and {@link Settings},
 * in other words, the same priority from Eclipse MicroProfile Configuration and JSR 382.
 */
public interface SettingsEncryption {

    String encrypt(String property);

    String decrypt(String property);
}
