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
import jakarta.data.page.Pageable;
import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.document.MappingDocumentQuery;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

/**
 * The {@link PageableRepository} template method
 */
public abstract class AbstractDocumentRepository<T, K> extends AbstractRepository<T, K> {

    protected abstract JNoSQLDocumentTemplate template();

    @Override
    public long count() {
        return template().count(type());
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable is required");
        EntityMetadata metadata = entityMetadata();
        DocumentQuery query = new MappingDocumentQuery(pageable.sorts(),
                pageable.size(), NoSQLPage.skip(pageable)
                , null, metadata.name());

        List<T> entities = template().<T>select(query).toList();
        return NoSQLPage.of(entities, pageable);
    }

    @Override
    public Stream<T> findAll() {
        return template().findAll(type());
    }

    @Override
    public void deleteAll() {
        template().deleteAll(type());
    }
}
