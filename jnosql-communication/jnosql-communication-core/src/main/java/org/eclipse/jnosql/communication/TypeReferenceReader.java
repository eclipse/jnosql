/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication;

import java.util.function.Predicate;

/**
 * The reader to {@link TypeReference}
 * The {@link Predicate} verifies if the TypeReferenceReader has the support of instance from this class.
 *
 * @see Value#get(TypeSupplier)
 */
public interface TypeReferenceReader extends Predicate<TypeSupplier<?>> {

    /**
     * converts to defined type on {@link TypeReference}
     *
     * @param typeReference the typeReference
     * @param value         the value
     * @param <T>           the typeReference type
     * @return the instance converted
     */
    <T> T convert(TypeSupplier<T> typeReference, Object value);

}
