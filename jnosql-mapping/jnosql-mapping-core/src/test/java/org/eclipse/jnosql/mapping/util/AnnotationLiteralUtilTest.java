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
package org.eclipse.jnosql.mapping.util;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Named;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationLiteralUtilTest {

    @Test
    public void shouldDefaultAnnotationLiteral() {
        AnnotationLiteral<Default> defaultAnnotation = AnnotationLiteralUtil.DEFAULT_ANNOTATION;
        assertEquals(Default.class, defaultAnnotation.annotationType());
    }

    @Test
    public void shouldAnyAnnotationLiteral() {
        AnnotationLiteral<Any> anyAnnotation = AnnotationLiteralUtil.ANY_ANNOTATION;

        assertEquals(Any.class, anyAnnotation.annotationType());
    }

    @Test
    public void shouldCustomAnnotationLiteral() {
        AnnotationLiteral<Named> namedLiteral = NamedLiteral.of("test");

        assertEquals(Named.class, namedLiteral.annotationType());
    }

}