/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.keyvalue.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.keyvalue.KeyValue;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


@ApplicationScoped
public class CollectionStructure {


    private List<String> names;


    private Set<String> fruits;


    private Queue<String> orders;


    private Map<String, String> map;

    @Inject
    public CollectionStructure(@KeyValue("names") List<String> names,
                               @KeyValue("fruits") Set<String> fruits,
                               @KeyValue("orders") Queue<String> orders,
                               @KeyValue("map") Map<String, String> map) {
        this.names = names;
        this.fruits = fruits;
        this.orders = orders;
        this.map = map;
    }

    CollectionStructure() {
    }

    public List<String> names() {
        return names;
    }

    public Set<String> fruits() {
        return fruits;
    }

    public Queue<String> orders() {
        return orders;
    }

    public Map<String, String> map() {
        return map;
    }
}
