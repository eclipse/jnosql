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
package org.eclipse.jnosql.mapping.semistructured;


import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.metadata.FieldValue;

import java.util.List;

/**
 * This interface represents a specialized {@link FieldValue} designed for mapping to database columns.
 * Implementations of this interface are expected to provide methods for converting an entity to a list of elements.
 */
public interface AttributeFieldValue extends FieldValue {


    /**
     * Converts an entity attribute to a list of elements.
     *
     * @param converter  the entity converter used for conversion
     * @param converters the converters used for conversion
     * @param <X>        the type of the entity attribute
     * @param <Y>        the type of the database column
     * @return a list of elements representing the entity attribute
     */
    <X, Y> List<Element> toElements(EntityConverter converter, Converters converters);

}