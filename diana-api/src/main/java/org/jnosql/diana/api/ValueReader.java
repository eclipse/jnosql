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


/**
 * This interface represents the converters to be used in Value method,
 * so if there's a new type that the current API doesn't support just creates a new implementation and
 * load it by service load process.
 *
 * @see Value
 * @see Value#get(Class)
 */
public interface ValueReader {

    /**
     * verifies if the reader has support of instance from this class.
     *
     * @param <T>   the type
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */

    <T> boolean isCompatible(Class<T> clazz);

    /**
     * Once this implementation is compatible with the class type, the next step it converts  an
     * instance to this new one from the rightful class.
     *
     * @param clazz - the new instance class
     * @param value - instance to be converted
     * @param <T>   - the new type class
     * @return a new instance converted from required class
     */
    <T> T read(Class<T> clazz, Object value);

}
