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
package org.eclipse.jnosql.artemis.column.reactive.query;

import jakarta.nosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplate;

public class DefaultReactiveColumnRepository<T, K> extends AbstractReactiveColumnRepository<T, K> {

    private final ReactiveColumnTemplate template;

    private final ClassMapping mapping;

    DefaultReactiveColumnRepository(ReactiveColumnTemplate template, ClassMapping mapping) {
        this.template = template;
        this.mapping = mapping;
    }

    @Override
    protected ReactiveColumnTemplate getTemplate() {
        return template;
    }

    @Override
    protected ClassMapping getClassMapping() {
        return mapping;
    }
}
