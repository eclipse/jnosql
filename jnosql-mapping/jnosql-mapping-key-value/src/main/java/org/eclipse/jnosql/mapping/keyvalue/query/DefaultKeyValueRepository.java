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
package org.eclipse.jnosql.mapping.keyvalue.query;


import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;

class DefaultKeyValueRepository<T>  extends AbstractKeyValueRepository<T> {


    private final KeyValueTemplate repository;

    private final EntityMetadata metadata;

    public DefaultKeyValueRepository(Class<T> typeClass, EntityMetadata metadata, KeyValueTemplate repository) {
        super(typeClass);
        this.repository = repository;
        this.metadata = metadata;
    }

    @Override
    protected KeyValueTemplate getTemplate() {
        return repository;
    }

    @Override
    protected EntityMetadata getEntityMetadata() {
        return metadata;
    }

}