/*
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
 */
package org.eclipse.jnosql.mapping;

import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@CDIExtension
class DefaultConvertersTest {

    @Inject
    private Converters converters;


    @Test
    public void shouldReturnNPEWhenClassIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> converters.get(null));
    }

    @Test
    public void shouldCreateAttributeConverterWithInjections() {
        AttributeConverter attributeConverter = converters.get(MyConverter.class);
        Object text = attributeConverter.convertToDatabaseColumn("Text");
        Assertions.assertNotNull(text);
    }

    @Test
    public void shouldCreateNotUsingInjections() {
        AttributeConverter attributeConverter = converters.get(VetedConverter.class);
        Object text = attributeConverter.convertToDatabaseColumn("Text");
        Assertions.assertNotNull(text);
        Assertions.assertEquals("Text", text);
    }

}