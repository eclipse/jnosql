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
 *   Maximillian Arruda
 */
package org.eclipse.jnosql.mapping.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {


    @Test
    public void testIsBlankWithNull() {
        assertTrue(StringUtils.isBlank(null));
    }

    @Test
    public void testIsBlankWithEmptyString() {
        assertTrue(StringUtils.isBlank(""));
    }

    @Test
    public void testIsBlankWithSpaces() {
        assertTrue(StringUtils.isBlank("   "));
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        assertFalse(StringUtils.isBlank("Hello, world!"));
    }

    @Test
    public void testIsNotBlankWithNull() {
        assertFalse(StringUtils.isNotBlank(null));
    }

    @Test
    public void testIsNotBlankWithEmptyString() {
        assertFalse(StringUtils.isNotBlank(""));
    }

    @Test
    public void testIsNotBlankWithSpaces() {
        assertFalse(StringUtils.isNotBlank("   "));
    }

    @Test
    public void testIsNotBlankWithNonBlankString() {
        assertTrue(StringUtils.isNotBlank("Hello, world!"));
    }

}