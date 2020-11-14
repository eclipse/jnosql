/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.keyvalue.reactive.query;

import org.eclipse.jnosql.mapping.keyvalue.reactive.ReactiveKeyValueTemplate;

class DefaultReactiveKeyValueRepository<T, K> extends AbstractReactiveKeyValueRepository<T, K>{

    private final ReactiveKeyValueTemplate template;

    public DefaultReactiveKeyValueRepository(Class<T> typeClass, ReactiveKeyValueTemplate template) {
        super(typeClass);
        this.template = template;
    }

    @Override
    protected ReactiveKeyValueTemplate getTemplate() {
        return template;
    }
}
