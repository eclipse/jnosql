/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Condition;

/**
 * The default implementation of {@link DocumentConditionProvider}
 */
public final class DocumentConditionProvider {

    public DocumentCondition between(Document document) {
        return DocumentCondition.between(document);
    }

    public DocumentCondition and(DocumentCondition... conditions) {
        return DocumentCondition.and(conditions);
    }

    public DocumentCondition or(DocumentCondition... conditions) {
        return DocumentCondition.or(conditions);
    }

    public DocumentCondition in(Document document) {
        return DocumentCondition.in(document);
    }

    public DocumentCondition apply(Document document, Condition condition) {
        return DocumentCondition.of(document, condition);
    }
}
