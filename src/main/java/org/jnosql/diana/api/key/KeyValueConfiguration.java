/*
 * Copyright 2017 Otavio Santana and others
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

/**
 * The diana configuration to create a {@link BucketManagerFactory}
 *
 * @param <T> the BucketManagerFactory type
 */
public interface KeyValueConfiguration<T extends BucketManagerFactory> {

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider
     * and then creates a {@link BucketManagerFactory} instance.
     *
     * @return a {@link BucketManagerFactory} instance
     */
    T get();
}
