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

import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.BasicRepository;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.document.MappingDocumentQuery;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * The {@link BasicRepository} template method
 */
public abstract class AbstractDocumentRepository<T, K> extends AbstractRepository<T, K> {

    protected abstract JNoSQLDocumentTemplate template();

    @Override
    public long countBy() {
        return template().count(type());
    }

    @Override
    public Page<T> findAll(PageRequest pageRequest) {
        Objects.requireNonNull(pageRequest, "pageRequest is required");
        EntityMetadata metadata = entityMetadata();
        DocumentQuery query = new MappingDocumentQuery(pageRequest.sorts(),
                pageRequest.size(), NoSQLPage.skip(pageRequest)
                , null, metadata.name());

        List<T> entities = template().<T>select(query).toList();
        return NoSQLPage.of(entities, pageRequest);
    }

    @Override
    public Stream<T> findAll() {
        return template().findAll(type());
    }
}
