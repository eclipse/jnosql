/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.diana.api.column;


import org.apache.diana.api.Value;

import java.io.Serializable;

/**
 * A Column Family entity unit, it is a tuple (pair) that consists of a key-value pair, where the key is mapped to a value.
 *
 * @author Otávio Santana
 */
public interface Column extends Serializable {


    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    static Column of(String name, Value value) {
        return new DefaultColumn(name, value);
    }

    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    static Column of(String name, Object value) {
        return new DefaultColumn(name, Value.of(value));
    }

    /**
     * The column's name
     *
     * @return name
     */
    String getName();

    /**
     * the column's value
     *
     * @return {@link Value}
     */
    Value getValue();
}
