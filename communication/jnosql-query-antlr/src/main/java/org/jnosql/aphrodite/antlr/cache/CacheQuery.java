/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr.cache;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;

import static java.util.Collections.synchronizedMap;

final class CacheQuery<V> {

    private final Map<String, V> store = synchronizedMap(new WeakHashMap<>());
    private final Map<String, WeakReference<String>> mutex = synchronizedMap(new WeakHashMap<>());
    private final Function<String, V> supplier;

    private CacheQuery(Function<String, V> supplier) {
        this.supplier = supplier;
    }


    public V get(String key) {
        V value = this.store.get(key);
        if (Objects.isNull(value)) {
            String synchronizedKey = mutex.computeIfAbsent(key, (a) -> new WeakReference<>(key)).get();
            synchronized (synchronizedKey) {
                value = supplier.apply(synchronizedKey);
                put(synchronizedKey, value);
            }
        }
        return value;
    }

    public int size() {
        return store.size();
    }

    private V put(String key, V value) {
        return store.put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CacheQuery{");
        sb.append("store=").append(store);
        sb.append('}');
        return sb.toString();
    }

    public static <V> CacheQuery<V> of(Function<String, V> supplier) {
        return new CacheQuery<>(supplier);
    }

}
