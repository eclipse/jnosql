/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IdNotFoundExceptionTest {
    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Test error message";
        IdNotFoundException exception = new IdNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testNewInstance() {
        Class<?> entityType = MyClass.class;
        String expectedMessage = "The entity " + entityType.getName() + " must have a field annotated with @Id";
        IdNotFoundException exception = IdNotFoundException.newInstance(entityType);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testKeyNotFoundExceptionSupplier() {
        Supplier<IdNotFoundException> supplier = IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;
        assertNotNull(supplier);
        IdNotFoundException exception = supplier.get();
        assertEquals("To use this resource you must annotated a field with @Id", exception.getMessage());
    }

    private static class MyClass {
    }
}