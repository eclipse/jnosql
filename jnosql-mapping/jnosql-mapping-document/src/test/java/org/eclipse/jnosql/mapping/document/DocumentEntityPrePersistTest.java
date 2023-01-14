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
package org.eclipse.jnosql.mapping.document;

import org.eclipse.jnosql.communication.document.DocumentEntity;
import jakarta.nosql.document.DocumentEntityPrePersist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DocumentEntityPrePersistTest {

    @Test
    public void shouldReturnNPEWhenEntityIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new DocumentEntityPrePersist(null));
    }

    @Test
    public void shouldReturnInstance() {
        DocumentEntity entity = DocumentEntity.of("collection");
        DocumentEntityPrePersist prePersist = new DocumentEntityPrePersist(entity);
        assertEquals(entity, prePersist.getEntity());
    }
}