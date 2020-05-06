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

import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ObservableTest {


    @Test
    public void shouldReturnInstance() {
        Publisher<Animal> predicate = ReactiveStreams
                .fromIterable(Arrays.asList(new Animal("Lion"))).buildRs();
        Assertions.assertNotNull(predicate);
    }


}