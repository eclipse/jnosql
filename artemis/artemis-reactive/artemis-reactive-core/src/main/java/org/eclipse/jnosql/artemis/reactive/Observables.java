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
package org.eclipse.jnosql.artemis.reactive;

import org.reactivestreams.Publisher;

import static java.util.Objects.requireNonNull;

/**
 * it is a wrapper of Publisher that observers and give several options to this instance.
 *
 * @param <T> the Entity
 */
public interface Observables<T> {


    /**
     * Creates a {@link Observables} instance
     *
     * @param publisher the publisher
     * @param <T>       the entity type
     * @return a instance of {@link Observables}
     * @throws NullPointerException when publisher is null
     */
    static <T> Observables<T> of(Publisher<T> publisher) {
        return new DefaultObservables<>(requireNonNull(publisher, "publisher"));
    }

}
