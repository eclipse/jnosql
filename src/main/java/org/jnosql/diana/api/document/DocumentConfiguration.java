/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.document;

/**
 * The diana configuration to create a {@link DocumentCollectionManagerFactory}
 * and {@link DocumentCollectionManagerAsyncFactory}
 *
 * @param <SYNC>  the type to DocumentCollectionManagerFactory
 */
public interface DocumentConfiguration<SYNC extends DocumentCollectionManagerFactory> {


    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider and then creates a
     * {@link DocumentCollectionManagerFactory} instance.
     *
     * @return a {@link DocumentCollectionManagerFactory} instance
     * @throws UnsupportedOperationException when the operation is not supported
     */
    SYNC get() throws UnsupportedOperationException;


}
