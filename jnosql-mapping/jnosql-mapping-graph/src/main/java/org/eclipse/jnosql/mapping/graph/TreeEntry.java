/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map;

record TreeEntry<K, V>(K key, V value) implements Map.Entry<K, V> {

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V v) {
        throw new UnsupportedOperationException("This entry is read-only");
    }


    static <K, V> TreeEntry<K, V> of(Vertex vertex, GraphConverter converter) {
        K key = (K) vertex.id();
        V value = converter.toEntity(vertex);
        return new TreeEntry<>(key, value);
    }

}
