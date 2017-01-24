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

package org.jnosql.diana.api.key;


import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * {@link BucketManager} factory.
 * When the application has finished using the bucket manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 */
public interface BucketManagerFactory<T extends BucketManager> extends AutoCloseable {

    /**
     * Creates a {@link BucketManager} from a bucket name
     *
     * @param bucketName a bucket name
     * @return a {@link BucketManager} instance
     * @throws UnsupportedOperationException when the database does not have to it
     * @throws NullPointerException          when bucketName is null
     */
    T getBucketManager(String bucketName) throws UnsupportedOperationException, NullPointerException;

    /**
     * Creates a {@link List} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link List} instance
     * @throws UnsupportedOperationException when the database does not have to it
     * @throws NullPointerException          when either bucketName or class are null
     */
    <T> List<T> getList(String bucketName, Class<T> clazz) throws UnsupportedOperationException, NullPointerException;

    /**
     * Creates a {@link Set} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link Set} instance
     * @throws UnsupportedOperationException when the database does not have to it
     * @throws NullPointerException          when either bucketName or class are null
     */
    <T> Set<T> getSet(String bucketName, Class<T> clazz) throws UnsupportedOperationException, NullPointerException;

    /**
     * Creates a {@link Queue} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link Queue} instance
     * @throws UnsupportedOperationException when the database does not have to it
     * @throws NullPointerException          when either bucketName or class are null
     */
    <T> Queue<T> getQueue(String bucketName, Class<T> clazz) throws UnsupportedOperationException, NullPointerException;

    /**
     * Creates a {@link  Map} from bucket name
     *
     * @param bucketName the bucket name
     * @param keyValue   the key class
     * @param valueValue the value class
     * @param <K>        the key type
     * @param <V>        the value type
     * @return a {@link Map} instance
     * @throws UnsupportedOperationException when the database does not have to it
     * @throws NullPointerException          when either bucketName or class are null
     */
    <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) throws
            UnsupportedOperationException, NullPointerException;

    /**
     * closes a resource
     */
    void close();
}
