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
package org.eclipse.jnosql.mapping.validation;

import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentEntity;
import org.mockito.Mockito;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;
import java.math.BigDecimal;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
class DocumentMockProducer implements Supplier<DocumentManager> {

    @Override
    @Produces
    public DocumentManager get() {
        DocumentManager collectionManager = Mockito.mock(DocumentManager.class);

        DocumentEntity entity = DocumentEntity.of("person");
        entity.add(Document.of("name", "Ada"));
        entity.add(Document.of("age", 30));
        entity.add(Document.of("salary", BigDecimal.TEN));
        entity.add(Document.of("phones", singletonList("22342342")));

        when(collectionManager.insert(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        when(collectionManager.update(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        return collectionManager;
    }
}
