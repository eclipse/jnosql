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

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.jnosql.diana.api.SettingsEncryption.ENCRYPTION_TYPE;

enum SettingsEncryptionSupplier implements Supplier<SettingsEncryption> {

    INSTANCE;

    private final List<SettingsEncryption> encrypts;

    private final AsymmetricSettingsEncryption asymmetric = new AsymmetricSettingsEncryption();

    private final SymmetricSettingsEncryption symmetric = new SymmetricSettingsEncryption();

    {
        ServiceLoader<SettingsEncryption> services = ServiceLoader.load(SettingsEncryption.class);
        this.encrypts = StreamSupport.stream(services.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public SettingsEncryption get() {
        return get(Settings.of(Collections.emptyMap()));
    }

    public SettingsEncryption get(Settings settings) {
        String property = SettingsPriority
                .get(ENCRYPTION_TYPE, settings)
                .map(Object::toString)
                .orElseThrow(() -> new EncryptionException("To use encryption in property settings set the property: " +
                        ENCRYPTION_TYPE));

        if ("symmetric".equals(property)) {
            return symmetric;
        } else if ("asymmetric".equals(property)) {
            return asymmetric;
        }

        return encrypts.stream()
                .filter(s -> s.getClass().getName().equals(property))
                .findFirst()
                .orElseThrow(() -> new EncryptionException("There is no implementation with this Class#getName" +
                        " at the property: " + ENCRYPTION_TYPE));
    }
}
