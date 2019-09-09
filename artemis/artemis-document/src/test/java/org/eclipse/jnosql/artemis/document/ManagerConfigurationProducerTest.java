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
package org.eclipse.jnosql.artemis.document;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.mapping.ConfigurationUnit;
import org.eclipse.jnosql.artemis.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@ExtendWith(CDIExtension.class)
public class ManagerConfigurationProducerTest {

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentCollectionManager manager;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentCollectionManagerAsync managerAsync;

    @Test
    public void shouldTestManager() {
        Assertions.assertNotNull(manager);
    }

    @Test
    public void shouldTestManagerAsync() {
        Assertions.assertNotNull(manager);
    }

}
