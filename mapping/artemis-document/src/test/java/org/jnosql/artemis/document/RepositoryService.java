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

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.PersonRepository;
import org.jnosql.artemis.PersonRepositoryAsync;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RepositoryService {

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentRepositorySupplier<PersonRepository> repository;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentRepositoryAsyncSupplier<PersonRepositoryAsync> repositoryAsync;

    public DocumentRepositorySupplier<PersonRepository> getRepository() {
        return repository;
    }

    public DocumentRepositoryAsyncSupplier<PersonRepositoryAsync> getRepositoryAsync() {
        return repositoryAsync;
    }


}
