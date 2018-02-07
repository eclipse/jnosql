/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
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
    T getBucketManager(String bucketName);

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
    <T> List<T> getList(String bucketName, Class<T> clazz);

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
    <T> Set<T> getSet(String bucketName, Class<T> clazz);

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
    <T> Queue<T> getQueue(String bucketName, Class<T> clazz);

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
    <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue);

    /**
     * closes a resource
     */
    void close();
}
