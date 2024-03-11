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
package org.eclipse.jnosql.mapping.graph;

import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Transaction.Status;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.BookTemplate;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.apache.tinkerpop.gremlin.structure.Transaction.Status.COMMIT;
import static org.apache.tinkerpop.gremlin.structure.Transaction.Status.ROLLBACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class, GraphTemplate.class})
@AddPackages(GraphProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class BookTemplateTest {

    @Inject
    private BookTemplate template;

    @Inject
    private Graph graph;

    @Test
    void shouldSaveWithTransaction() {
        AtomicReference<Status> status = new AtomicReference<>();

        Book book = Book.builder().withName("The Book").build();
        Transaction transaction = graph.tx();
        transaction.addTransactionListener(status::set);
        template.insert(book);
        assertFalse(transaction.isOpen());
        assertEquals(COMMIT, status.get());
    }

    @Test
    void shouldSaveWithRollback() {
        AtomicReference<Status> status = new AtomicReference<>();

        Book book = Book.builder().withName("The Book").build();
        Transaction transaction = graph.tx();
        transaction.addTransactionListener(status::set);
        try {
            template.insertException(book);
            assert false;
        }catch (Exception ignored){

        }

        assertFalse(transaction.isOpen());
        assertEquals(ROLLBACK, status.get());
    }

    @Test
    void shouldUseAutomaticNormalTransaction() {
        AtomicReference<Status> status = new AtomicReference<>();

        Book book = Book.builder().withName("The Book").build();
        Transaction transaction = graph.tx();
        transaction.addTransactionListener(status::set);
        assertNull(status.get());
        template.normalInsertion(book);
        assertEquals(COMMIT, status.get());
        assertFalse(transaction.isOpen());
    }
}

