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

import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;

import java.util.List;

/**
 * The ConstructorMetadata interface provides information about a Java constructor.
 * It contains methods to retrieve details about the constructor and its parameters.
 *
 */
public interface ConstructorMetadata {

    /**
     * Returns a list of ParameterMetaData objects representing the parameters of the constructor.
     *
     * @return the constructor parameters
     */
    List<ParameterMetaData> parameters();

    /**
     * Checks if the constructor is a default (no-argument) constructor.
     *
     * @return {@code true} if the constructor is a default constructor, otherwise {@code false}
     */
    boolean isDefault();
}
