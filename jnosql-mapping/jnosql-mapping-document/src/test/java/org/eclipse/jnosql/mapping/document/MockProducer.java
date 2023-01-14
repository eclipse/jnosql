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


import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import org.mockito.Mockito;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;
import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class MockProducer implements Supplier<DocumentManager> {

    @Produces
    @Override
    public DocumentManager get() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.add(Document.of("name", "Default"));
        entity.add(Document.of("age", 10));
        DocumentManager manager = mock(DocumentManager.class);
        when(manager.insert(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        return manager;
    }

    @Produces
    @Database(value = DatabaseType.DOCUMENT, provider = "documentRepositoryMock")
    public DocumentManager getDocumentManagerMock() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.add(Document.of("name", "documentRepositoryMock"));
        entity.add(Document.of("age", 10));
        DocumentManager manager = mock(DocumentManager.class);
        when(manager.insert(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        when(manager.singleResult(Mockito.any(DocumentQuery.class))).thenReturn(Optional.empty());
        return manager;

    }
}
