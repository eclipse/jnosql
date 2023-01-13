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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.tck.entities.ZipCode;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.InstanceProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;


@CDIExtension
public class InstanceProducerTest {

    @Inject
    private InstanceProducer producer;

    @Test
    public void shouldCreateNewInstance() {

        ZipCode zipcode = producer.create(ZipCode.class);
        Assertions.assertNotNull(zipcode);
        Assertions.assertTrue(zipcode instanceof ZipCode);
    }
    @Test
    public void shouldReturnNPEWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> producer.create(null));
    }


    @Test
    public void shouldCreateInstance() {
        Instance instance = producer.create(Instance.class);
        Assertions.assertNotNull(instance);
    }


    public static class Instance {

    }

}
