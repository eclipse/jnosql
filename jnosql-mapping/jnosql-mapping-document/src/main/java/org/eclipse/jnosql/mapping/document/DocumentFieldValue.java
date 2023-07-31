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
import org.eclipse.jnosql.mapping.Converters;

import org.eclipse.jnosql.mapping.metadata.FieldValue;

import java.util.List;

/**
 * The specialist {@link FieldValue} to document
 */
public interface DocumentFieldValue extends FieldValue {

    /**
     * Converts an entity to a {@link List} of documents
     * @param converter the converter
     * @param converters the converters
     * @param <X> the type of the entity attribute
     * @param <Y> the type of the database column
     * @return a {@link List} of documents from the field
     */
    <X, Y> List<Document> toDocument(DocumentEntityConverter converter, Converters converters);
}
