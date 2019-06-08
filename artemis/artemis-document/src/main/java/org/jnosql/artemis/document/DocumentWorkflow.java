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
package org.jnosql.artemis.document;


import org.jnosql.diana.api.document.DocumentEntity;

import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link DocumentTemplate}.
 * The default implementation follows:
 *  <p>{@link DocumentEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePreDocumentEntity(Object)}</p>
 *  <p>{@link DocumentEntityConverter#toDocument(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePreDocument(DocumentEntity)}}</p>
 *  <p>Database alteration</p>
 *  <p>{@link DocumentEventPersistManager#firePostDocument(DocumentEntity)}</p>
 *  <p>{@link DocumentEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePostDocumentEntity(Object)}</p>
 */
public interface DocumentWorkflow {

    /**
     * Executes the workflow to do an interaction on a database document collection.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the the entity response
     * @see DocumentTemplate#insert(Object, java.time.Duration) DocumentTemplate#insert(Object)
     * DocumentTemplate#update(Object)
     */
    <T> T flow(T entity, UnaryOperator<DocumentEntity> action);
}
