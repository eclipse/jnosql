/*
 *  Copyright (c) 2022 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.validation;

import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.test.CDIExtension;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

@CDIExtension
public class ConstructorValidationTest {

    //constructor valid values
    //constructor invalid values

    @Test
    public void shouldReturnErrorWhenInvalidConstructor() {
        Constructor<Computer> constructor = getConstructor();
        Object[] params = new Object[]{null, 2000, ""};

    }

    private Constructor<Computer> getConstructor() {
        try {
            return Computer.class.getConstructor(String.class,int.class, String.class);
        } catch (NoSuchMethodException error) {
            throw new MappingException("There is an error to find Computer constructor",error);
        }
    }
}
