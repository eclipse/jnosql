/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document;



import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link DocumentDeleteQuery}
 */
record DefaultDocumentDeleteQuery (String name,

        DocumentCondition documentCondition, List<String> documents) implements DocumentDeleteQuery {

    @Override
    public Optional<DocumentCondition> condition() {
        return ofNullable(documentCondition).map(DocumentCondition::readOnly);
    }

    @Override
    public List<String> documents() {
        return unmodifiableList(documents);
    }
}
