/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.document;

import jakarta.nosql.Condition;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentCondition.DocumentConditionProvider;

/**
 * The default implementation of {@link DocumentConditionProvider}
 */
public final class DefaultDocumentConditionProvider implements DocumentConditionProvider {

    @Override
    public DocumentCondition between(Document document) {
        return DefaultDocumentCondition.between(document);
    }

    @Override
    public DocumentCondition and(DocumentCondition... conditions) {
        return DefaultDocumentCondition.and(conditions);
    }

    @Override
    public DocumentCondition or(DocumentCondition... conditions) {
        return DefaultDocumentCondition.or(conditions);
    }

    @Override
    public DocumentCondition in(Document document) {
        return DefaultDocumentCondition.in(document);
    }

    @Override
    public DocumentCondition apply(Document document, Condition condition) {
        return DefaultDocumentCondition.of(document, condition);
    }
}
