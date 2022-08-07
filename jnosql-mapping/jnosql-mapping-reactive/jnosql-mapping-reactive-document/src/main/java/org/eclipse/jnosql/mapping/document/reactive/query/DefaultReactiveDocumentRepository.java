/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.document.reactive.query;

import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplate;

class DefaultReactiveDocumentRepository<T, K> extends AbstractReactiveDocumentRepository<T, K> {

    private final ReactiveDocumentTemplate template;

    private final EntityMetadata mapping;

    DefaultReactiveDocumentRepository(ReactiveDocumentTemplate template, EntityMetadata mapping) {
        this.template = template;
        this.mapping = mapping;
    }

    @Override
    protected ReactiveDocumentTemplate getTemplate() {
        return template;
    }

    @Override
    protected EntityMetadata getEntityMetadata() {
        return mapping;
    }
}
