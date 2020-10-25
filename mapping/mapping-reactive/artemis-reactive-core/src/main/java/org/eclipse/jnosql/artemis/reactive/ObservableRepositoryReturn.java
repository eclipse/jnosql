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

import org.eclipse.jnosql.artemis.repository.DynamicReturn;
import org.eclipse.jnosql.artemis.repository.RepositoryReturn;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.util.stream.Stream;

/**
 * A {@link RepositoryReturn} to {@link Observable}
 */
public class ObservableRepositoryReturn implements RepositoryReturn {

    @Override
    public boolean isCompatible(Class<?> entityClass, Class<?> returnType) {
        return Observable.class.equals(returnType);
    }

    @Override
    public <T> Object convert(DynamicReturn<T> dynamicReturn) {
        final Stream<T> result = dynamicReturn.result();
        return Observable.of(ReactiveStreams.fromIterable(result::iterator).buildRs());
    }

    @Override
    public <T> Object convertPageable(DynamicReturn<T> dynamicReturn) {
        final Stream<T> result = dynamicReturn.streamPagination();
        return Observable.of(ReactiveStreams.fromIterable(result::iterator).buildRs());
    }
}
