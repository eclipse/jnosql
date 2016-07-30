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

package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Class to reads and converts to {@link Enum}
 *
 * @author Otávio Santana
 */
public final class EnumReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Enum.class.isAssignableFrom(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {


        if (clazz.isInstance(value)) {
            return (T) value;
        }
        if (!Enum.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The informed class isn't an enum type: " + clazz);
        }
        List<Enum> elements = getEnumList((Class<Enum>) clazz);

        if (Number.class.isInstance(value)) {
            int index = Number.class.cast(value).intValue();
            return (T) elements.stream().filter(element -> element.ordinal() == index).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("There is not index in enum to value: " + index));
        }
        String name = value.toString();
        return (T) elements.stream().filter(element -> element.name().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There isn't name in enum to value: " + name));
    }

    private <T> List<Enum> getEnumList(Class<Enum> clazz) {
        Class<Enum> enumClass = clazz;
        EnumSet enumSet = EnumSet.allOf(enumClass);
        return new ArrayList<>(enumSet);
    }


}
