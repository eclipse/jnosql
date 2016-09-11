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

package org.jnosql.diana.api;

/**
 * To put your own Java Structure in NoSQL database is necessary convert it to a supported one.
 * So, the ValueWriter has the goal to convert to any specific structure type that a database might support.
 * These implementation will loaded by ServiceLoad and a NoSQL implementation will may use it.
 *
 * @param <T> current type
 * @param <S> the converted type
 */
public interface ValueWriter<T, S> {

    /**
     * verifies if the writer has support of instance from this class.
     *
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */
    boolean isCompatible(Class clazz);

    /**
     * Converts a specific structure to a new one.
     *
     * @param object the instance to be converted
     * @return a new instance with the new class
     */

    S write(T object);
}
