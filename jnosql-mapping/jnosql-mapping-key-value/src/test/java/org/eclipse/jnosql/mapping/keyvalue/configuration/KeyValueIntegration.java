/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.keyvalue.configuration;


import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.BucketManagerFactory;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KeyValueIntegration {

    private static final String KEY_VALUE = "keyvalue";

    @Inject
    @ConfigProperty(name = KEY_VALUE)
    private KeyValueTemplate template;

    @Inject
    @ConfigProperty(name = KEY_VALUE)
    private BucketManager manager;

    @Inject
    @ConfigProperty(name = KEY_VALUE)
    private BucketManagerFactory factory;

    public KeyValueTemplate getTemplate() {
        return template;
    }

    public BucketManager getManager() {
        return manager;
    }

    public BucketManagerFactory getFactory() {
        return factory;
    }
}
