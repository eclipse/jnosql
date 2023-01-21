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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.Template;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.test.jupiter.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.eclipse.jnosql.mapping.DatabaseType.COLUMN;

@CDIExtension
class ColumnTemplateTest {

    @Inject
    private Template template;

    @Inject
    @Database(COLUMN)
    private Template qualifier;

    @Test
    public void shouldInjectTemplate() {
        Assertions.assertNotNull(template);
    }

    @Test
    public void shouldInjectQualifier() {
        Assertions.assertNotNull(qualifier);
    }
}
