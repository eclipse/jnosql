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

package org.jnosql.diana;


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
