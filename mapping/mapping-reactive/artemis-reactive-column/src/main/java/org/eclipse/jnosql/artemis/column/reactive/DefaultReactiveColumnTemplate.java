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
package org.eclipse.jnosql.artemis.column.reactive;

import jakarta.nosql.mapping.column.ColumnTemplate;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

class DefaultReactiveColumnTemplate extends AbstractReactiveColumnTemplate {

    private Instance<ColumnTemplate> template;

    @Inject
    DefaultReactiveColumnTemplate(Instance<ColumnTemplate> template) {
        this.template = template;
    }

    DefaultReactiveColumnTemplate() {
    }

    @Override
    protected ColumnTemplate getTemplate() {
        return template.get();
    }
}
