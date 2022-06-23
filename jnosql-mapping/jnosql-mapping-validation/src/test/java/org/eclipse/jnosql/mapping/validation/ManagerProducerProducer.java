/*
 *  Copyright (c) 2017 Otávio Santana and others
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


import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.keyvalue.BucketManager;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;
import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

public class ManagerProducerProducer {


    @Produces
    public BucketManager getBucketManager() {
        return Mockito.mock(BucketManager.class);
    }

    @Produces
    public DocumentCollectionManager getDocumentCollectionManager() {
        DocumentCollectionManager collectionManager = Mockito.mock(DocumentCollectionManager.class);


        DocumentEntity entity = DocumentEntity.of("person");
        entity.add(Document.of("name", "Ada"));
        entity.add(Document.of("age", 10));
        entity.add(Document.of("salary", BigDecimal.TEN));
        entity.add(Document.of("phones", singletonList("22342342")));

        when(collectionManager.insert(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        when(collectionManager.update(Mockito.any(DocumentEntity.class))).thenReturn(entity);
        return collectionManager;
    }

    @Produces
    public ColumnFamilyManager getColumnFamilyManager() {
        ColumnFamilyManager columnFamilyManager = Mockito.mock(ColumnFamilyManager.class);

        ColumnEntity entity = ColumnEntity.of("person");
        entity.add(Column.of("name", "Ada"));
        entity.add(Column.of("age", 10));
        entity.add(Column.of("salary", BigDecimal.TEN));
        entity.add(Column.of("phones", singletonList("22342342")));

        when(columnFamilyManager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        when(columnFamilyManager.update(Mockito.any(ColumnEntity.class))).thenReturn(entity);


        return columnFamilyManager;
    }
}
