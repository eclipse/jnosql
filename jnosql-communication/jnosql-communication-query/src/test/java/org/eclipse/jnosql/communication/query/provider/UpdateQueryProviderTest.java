/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.provider;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.query.UpdateQuery.UpdateQueryProvider;
import org.eclipse.jnosql.communication.query.cache.CachedUpdateQueryProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

public class UpdateQueryProviderTest {

    @BeforeEach
    public void setUp() {
        Module module = DeleteQueryProviderTest.class.getModule();
        module.addUses(UpdateQueryProvider.class);
    }
    @Test
    public void shouldGetSupplier() {
        UpdateQueryProvider provider = ServiceLoaderProvider.get(UpdateQueryProvider.class,
                ()-> ServiceLoader.load(UpdateQueryProvider.class));
        Assertions.assertNotNull(provider);
        Assertions.assertTrue(provider instanceof CachedUpdateQueryProvider);
    }
}
