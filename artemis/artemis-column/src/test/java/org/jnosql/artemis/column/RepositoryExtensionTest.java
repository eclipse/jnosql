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
package org.jnosql.artemis.column;

import jakarta.nosql.mapping.ConfigurationUnit;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.PersonRepositoryAsync;
import org.jnosql.artemis.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static jakarta.nosql.mapping.DatabaseType.COLUMN;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(CDIExtension.class)
public class RepositoryExtensionTest {



    @Inject
    @ConfigurationUnit(database = "name", fileName = "column.json", name = "name")
    private UserRepository repository;


    @Inject
    @ConfigurationUnit(database = "name", fileName = "column.json", name = "name", repository = COLUMN)
    private UserRepository repository2;


    @Inject
    @ConfigurationUnit(database = "name", fileName = "column.json", name = "name")
    private PersonRepositoryAsync repositoryAsync;

    @Inject
    @ConfigurationUnit(database = "name", fileName = "column.json", name = "name", repository = COLUMN)
    private PersonRepositoryAsync repositoryAsync2;



    @Test
    public void shouldUseUserRepository() {
        assertNotNull(repository);

    }

    @Test
    public void shouldUseUserRepository2() {
        assertNotNull(repository2);

    }

    @Test
    public void shouldUseUserRepositoryAsync() {
        assertNotNull(repositoryAsync);

    }

    @Test
    public void shouldUseUserRepositoryAsync2() {
        assertNotNull(repositoryAsync2);

    }
}
