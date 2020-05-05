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


/**
 * The producer of {@link ReactiveColumnTemplate}
 */
public interface ReactiveColumnTemplateProducer {

    /**
     * creates a {@link ReactiveColumnTemplate}
     *
     * @param <T>     the KeyValueTemplate instance
     * @param template the template
     * @return a new instance
     * @throws NullPointerException when template is null
     */
    <T extends ReactiveColumnTemplate> T get(ColumnTemplate template);
}
