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

package org.jnosql.diana.api.column;

import org.jnosql.diana.api.document.DocumentCondition;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A unit that has the collection and condition to delete from conditions
 *
 * @see ColumnDeleteCondition#of(String, DocumentCondition).
 * This instance will be used on:
 * {@link ColumnFamilyManager#delete(ColumnDeleteCondition)}
 * {@link ColumnFamilyManagerAsync#delete(ColumnDeleteCondition)}
 * {@link ColumnFamilyManagerAsync#delete(ColumnDeleteCondition, Consumer)}
 */
public class ColumnDeleteCondition {

    private final String collection;

    private final DocumentCondition condition;

    private ColumnDeleteCondition(String collection, DocumentCondition condition) {
        this.collection = collection;
        this.condition = condition;
    }

    /**
     * getter the collection name
     *
     * @return the collection name
     */
    public String getCollection() {
        return collection;
    }

    /**
     * getter the condition
     *
     * @return the condition
     */
    public DocumentCondition getCondition() {
        return condition;
    }

    /**
     * Creates a instance
     *
     * @param collection
     * @param condition
     * @return
     * @throws NullPointerException
     */
    public ColumnDeleteCondition of(String collection, DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(collection, "collection is required");
        Objects.requireNonNull(condition, "condition is required");
        return new ColumnDeleteCondition(collection, condition);
    }
}
