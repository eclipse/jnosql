/*
 *  Copyright (c) 2020 Otávio Santana and others
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
 * It represents the operations within a class such as create a new instance,
 * setter, and getter. These classes are represented with {@link InstanceSupplier}
 * {@link FieldWriter} and {@link FieldReader} respectively.
 */
public interface ClassOperation {

    /**
     * Returns a factory of {@link InstanceSupplier}
     * @return a {@link InstanceSupplier} instance
     */
    InstanceSupplierFactory getInstanceSupplierFactory();

    /**
     * Returns a factory of {@link FieldWriterFactory}
     * @return a {@link FieldWriterFactory}
     */
    FieldWriterFactory getFieldWriterFactory();

    /**
     * Returns a factory of {@link FieldReaderFactory}
     * @return a {@link FieldReaderFactory}
     */
    FieldReaderFactory getFieldReaderFactory();
}

