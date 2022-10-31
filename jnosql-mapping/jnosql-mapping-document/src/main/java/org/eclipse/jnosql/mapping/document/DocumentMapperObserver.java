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

import jakarta.nosql.document.DocumentObserverParser;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import java.util.Optional;

final class DocumentMapperObserver  implements DocumentObserverParser {


    private final EntitiesMetadata mappings;

    DocumentMapperObserver(EntitiesMetadata mappings) {
        this.mappings = mappings;
    }


    @Override
    public String fireEntity(String entity) {
        Optional<EntityMetadata> entityMetadata = getEntityMetadata(entity);
        return entityMetadata.map(EntityMetadata::getName).orElse(entity);
    }

    @Override
    public String fireField(String entity, String field) {
        Optional<EntityMetadata> mapping = getEntityMetadata(entity);
        return mapping.map(c -> c.getColumnField(field)).orElse(field);
    }

    private Optional<EntityMetadata> getEntityMetadata(String entity) {
        Optional<EntityMetadata> bySimpleName = mappings.findBySimpleName(entity);
        if (bySimpleName.isPresent()) {
            return bySimpleName;
        }
        return mappings.findByClassName(entity);
    }

}
