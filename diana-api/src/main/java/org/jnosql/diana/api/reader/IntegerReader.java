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

package org.jnosql.diana.api.reader;


import org.jnosql.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Integer}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#intValue()} otherwise convert to {@link String} and then {@link Integer}
 *
 * @author Otávio Santana
 */
public final class IntegerReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Integer.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Integer.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Integer.valueOf(Number.class.cast(value).intValue());
        } else {
            return (T) Integer.valueOf(value.toString());
        }
    }
}
