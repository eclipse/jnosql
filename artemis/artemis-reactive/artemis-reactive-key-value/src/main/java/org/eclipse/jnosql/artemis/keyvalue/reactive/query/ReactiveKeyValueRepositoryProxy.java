/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.keyvalue.reactive.query;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;

class ReactiveKeyValueRepositoryProxy <T> extends AbstractReactiveKeyValueRepositoryProxy<T> {

    private final DefaultReactiveKeyValueRepository repository;
    private final KeyValueTemplate template;
    private final Class<T> entityClass;

    ReactiveKeyValueRepositoryProxy(DefaultReactiveKeyValueRepository repository,
                                    KeyValueTemplate template, Class<T> entityClass) {
        this.repository = repository;
        this.template = template;
        this.entityClass = entityClass;
    }

    @Override
    protected KeyValueTemplate getTemplate() {
        return template;
    }

    @Override
    protected ReactiveRepository getReactiveRepository() {
        return repository;
    }

    @Override
    protected Class<T> getEntityClass() {
        return entityClass;
    }
}
