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

package org.jnosql.diana.api.document;


/**
 * {@link DocumentCollectionManager} factory.
 * When the application has finished using the document collection manager factory, and/or at application shutdown,
 * the application should close the column family manager factory.
 *
 * @param <SYNC>  the {@link DocumentCollectionManager} type
 */
public interface DocumentCollectionManagerFactory<SYNC extends DocumentCollectionManager> extends AutoCloseable {

    /**
     * Creates a {@link DocumentCollectionManager} from database's name
     *
     * @param database a database name
     * @return a new {@link DocumentCollectionManager} instance
     * @throws UnsupportedOperationException when this operation is not supported
     *                                       throws {@link NullPointerException} when the database is null
     */
    SYNC get(String database) throws UnsupportedOperationException, NullPointerException;


    /**
     * closes a resource
     */
    void close();
}
