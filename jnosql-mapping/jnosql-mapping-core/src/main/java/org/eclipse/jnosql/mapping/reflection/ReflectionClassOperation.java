/*
 *  Copyright (c) 2018 Otávio Santana and others
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
 * An implementation of {@link ClassOperation} the supplier operations with Reflection
 */
class ReflectionClassOperation implements ClassOperation {

    private final ReflectionInstanceSupplierFactory supplierFactory;

    private final ReflectionFieldWriterFactory writerFactory;

    private final ReflectionFieldReaderFactory readerFactory;

    ReflectionClassOperation(Reflections reflections) {
        supplierFactory = new ReflectionInstanceSupplierFactory(reflections);
        writerFactory = new ReflectionFieldWriterFactory(reflections);
        readerFactory = new ReflectionFieldReaderFactory(reflections);
    }

    @Override
    public InstanceSupplierFactory getInstanceSupplierFactory() {
        return supplierFactory;
    }

    @Override
    public FieldWriterFactory getFieldWriterFactory() {
        return writerFactory;
    }

    @Override
    public FieldReaderFactory getFieldReaderFactory() {
        return readerFactory;
    }
}
