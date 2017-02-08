/*
 * Copyright 2017 Eclipse Foundation
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

package org.jnosql.diana.api;


import java.util.Objects;

/**
 * This element represents a required order to be used in a query, it's has two attributes:
 * -- The name - the field's name to be sorted
 * -- The type - the way to be sorted
 *
 * @see Sort#of(String, SortType)
 * @see SortType
 */
public class Sort {

    private final String name;

    private final SortType type;

    private Sort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a bew Sort instance to be used in a NoSQL query.
     *
     * @param name - the field name be used in a sort process
     * @param type - the way to be sorted
     * @return a sort instance
     */
    public static Sort of(String name, SortType type) {
        return new Sort(name, type);
    }

    public String getName() {
        return name;
    }

    public SortType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return Objects.equals(name, sort.name) &&
                type == sort.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sort{");
        sb.append("name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    /**
     * The way to be sorted.
     *
     * @see Sort
     */
    public enum SortType {
        /**
         * The ascending way
         */
        ASC,
        /**
         * The descending way
         */
        DESC
    }
}
