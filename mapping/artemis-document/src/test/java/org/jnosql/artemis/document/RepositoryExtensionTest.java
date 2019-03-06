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
package org.jnosql.artemis.document;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.jnosql.artemis.DatabaseType.DOCUMENT;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(CDIExtension.class)
public class RepositoryExtensionTest {


    @Inject
    @ConfigurationUnit(database = "name", fileName = "document.json", name = "name")
    private UserRepository repository;


    @Inject
    @ConfigurationUnit(database = "name", fileName = "document.json", name = "name", repository = DOCUMENT)
    private UserRepository repository2;



    @Test
    public void shouldUseUserRepository() {
        assertNotNull(repository);

    }

    @Test
    public void shouldUseUserRepository2() {
        assertNotNull(repository2);

    }
}
