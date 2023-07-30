/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.reflection;


/**
 * The tuple between the instance value and {@link FieldMetadata}
 */
public interface FieldValue {

    /**
     * Returns the instance
     *
     * @return the instance
     */
    Object value();

    /**
     * returns the {@link FieldMetadata}
     *
     * @return the {@link FieldMetadata} instance
     */
    FieldMetadata field();

    /**
     * Returns true if {@link FieldValue#value()} is different of null
     *
     * @return if {@link FieldValue#value()} is different of null
     */
    boolean isNotEmpty();

}
