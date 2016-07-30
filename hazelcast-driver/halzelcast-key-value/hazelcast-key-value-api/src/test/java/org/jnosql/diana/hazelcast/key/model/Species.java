/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.hazelcast.key.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Species implements Serializable {

    private static final long serialVersionUID = -1493508757572337719L;

    private final List<String> animals;

    public Species(String... animals) {
        this.animals = Arrays.asList(animals);
    }

    public List<String> getAnimals() {
        return animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Species species = (Species) o;
        return Objects.equals(animals, species.animals);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(animals);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Species{");
        sb.append("animals=").append(animals);
        sb.append('}');
        return sb.toString();
    }
}