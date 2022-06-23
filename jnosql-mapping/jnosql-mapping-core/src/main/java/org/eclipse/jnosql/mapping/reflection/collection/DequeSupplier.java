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
package org.eclipse.jnosql.mapping.reflection.collection;

import org.eclipse.jnosql.mapping.reflection.CollectionSupplier;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * An implementation of {@link CollectionSupplier} to {@link LinkedList}
 */
public class DequeSupplier implements CollectionSupplier<LinkedList<?>> {

    @Override
    public boolean test(Class<?> type) {
        return Deque.class.equals(type) || Queue.class.equals(type);
    }

    @Override
    public LinkedList<?> get() {
        return new LinkedList<>();
    }
}
