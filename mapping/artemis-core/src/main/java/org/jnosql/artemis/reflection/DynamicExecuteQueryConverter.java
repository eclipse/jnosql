/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.reflection;

import org.jnosql.artemis.Page;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

interface DynamicExecuteQueryConverter {

    <T> T toInstance(DynamicReturn<T> dynamicReturn);

    <T> Optional<T> toOptional(DynamicReturn<T> dynamicReturn);

    <T> List<T> toList(DynamicReturn<T> dynamicReturn);

    <T> Set<T> toSet(DynamicReturn<T> dynamicReturn);

    <T> Queue<T> toLinkedList(DynamicReturn<T> dynamicReturn);

    <T> Stream<T> toStream(DynamicReturn<T> dynamicReturn);

    <T> TreeSet<T> toTreeSet(DynamicReturn<T> dynamicReturn);

    <T> Object toDefault(DynamicReturn<T> dynamicReturn);

    <T> Page<T> toPage(DynamicReturn<?> dynamic);
}
