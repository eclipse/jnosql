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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.nosql.mapping.document.DocumentQueryMapper;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteFrom;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class DefaultDocumentQueryMapperTest {


    @Inject
    private DocumentQueryMapper queryMapper;


    @Test
    public void shouldReturnErrorWhenEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> queryMapper.selectFrom(null));
    }

    @Test
    public void shouldReturnSelectFrom() {
        DocumentQueryMapper.DocumentMapperFrom documentFrom = queryMapper.selectFrom(Person.class);
        assertNotNull(documentFrom);
    }

    @Test
    public void shouldReturnErrorWhenDeleteEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> queryMapper.deleteFrom(null));
    }

    @Test
    public void shouldReturnDeleteFrom() {
        DocumentMapperDeleteFrom deleteFrom = queryMapper.deleteFrom(Person.class);
        assertNotNull(deleteFrom);
    }
}