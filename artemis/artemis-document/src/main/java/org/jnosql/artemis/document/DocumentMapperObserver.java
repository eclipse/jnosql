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

import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.document.DocumentObserverParser;

import java.util.Optional;

final class DocumentMapperObserver  implements DocumentObserverParser {


    private final ClassMappings mappings;

    DocumentMapperObserver(ClassMappings mappings) {
        this.mappings = mappings;
    }


    @Override
    public String fireEntity(String entity) {
        Optional<ClassMapping> mapping = getClassMapping(entity);
        return mapping.map(ClassMapping::getName).orElse(entity);
    }

    @Override
    public String fireField(String entity, String field) {
        Optional<ClassMapping> mapping = getClassMapping(entity);
        return mapping.map(c -> c.getColumnField(field)).orElse(field);
    }

    private Optional<ClassMapping> getClassMapping(String entity) {
        Optional<ClassMapping> bySimpleName = mappings.findBySimpleName(entity);
        if (bySimpleName.isPresent()) {
            return bySimpleName;
        }
        return mappings.findByClassName(entity);
    }

}
