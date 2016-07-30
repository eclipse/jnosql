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


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface Value extends Serializable {

    /**
     * Returns the real value without conversion.
     *
     * @return the instance inside {@link Value}
     */
    Object get();

    /**
     * Cast class to specificed type
     * @param <T> the type
     * @return a class casted to defined type
     * @throws ClassCastException when cast is not possible
     */
    <T> T cast()throws ClassCastException;

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param clazz the new class
     * @param <T>   the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ReaderField
     */
    <T> T get(Class<T> clazz) throws NullPointerException, UnsupportedOperationException;


    /**
     * Converts the {@link Value#get()} in a {@link List},
     * if the value is not a Iterable extension it will be converted to a list with just one element.
     *
     * @param clazz type to be converted
     * @param <T>   - type
     * @return a instance list
     * @throws NullPointerException          - when clazz is null
     * @throws UnsupportedOperationException when the type is unsupported
     */
    <T> List<T> getList(Class<T> clazz) throws NullPointerException, UnsupportedOperationException;

    /**
     * Converts {@link Value#get()} in a {@link Set} type of class,
     * if the value is not a Iterable extension it will be converted to a Set with just one element.
     * type of class
     *
     * @param clazz type to be converted
     * @param <T>   - type
     * @return a instance of {@link Set}
     * @throws NullPointerException          - when clazz is null
     * @throws UnsupportedOperationException when the type is unsupported
     */
    <T> Set<T> getSet(Class<T> clazz) throws NullPointerException, UnsupportedOperationException;


    /**
     * Converts {@link Value#get()} to a {@link Map}
     *
     * @param keyClass   - the key class
     * @param valueClass - the value class
     * @param <K>        - the key type
     * @param <V>        - the value type
     * @return a {@link Map} instance
     * @throws NullPointerException          when clazz is null
     * @throws UnsupportedOperationException when the type is unsupported
     */
    <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) throws NullPointerException,
            UnsupportedOperationException;

    /**
     * Converts {@link Value#get()} in a {@link Stream} type of class,
     * @param clazz the class
     * @param <T> the type
     * @return a {@link Stream} instance
     */
    default <T> Stream<T> getStream(Class<T> clazz) {
        return getList(clazz).stream();
    }


    /**
     * Creates a new {@link Value} instance
     *
     * @param value - the information to {@link Value}
     * @return a {@link Value} instance within a value informed
     * @throws NullPointerException when the parameter is null
     */
    static Value of(Object value) throws NullPointerException {
        return DefaultValue.of(value);
    }

}
