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
package org.jnosql.artemis.reflection;

import jakarta.nosql.mapping.reflection.ClassOperation;
import jakarta.nosql.mapping.reflection.FieldReaderFactory;
import jakarta.nosql.mapping.reflection.FieldWriterFactory;
import jakarta.nosql.mapping.reflection.InstanceSupplierFactory;
import jakarta.nosql.mapping.reflection.Reflections;

final class JavaCompilerClassOperation implements ClassOperation {

    private final InstanceSupplierFactory instanceSupplierFactory;

    private final FieldWriterFactory fieldWriterFactory;

    private final FieldReaderFactory fieldReaderFactory;


    JavaCompilerClassOperation(ClassOperation fallback, Reflections reflections, JavaCompilerFacade compilerFacade) {
        this.instanceSupplierFactory = new JavaCompilerInstanceSupplierFactory(compilerFacade, reflections,
                fallback.getInstanceSupplierFactory());
        this.fieldWriterFactory = new JavaCompilerFieldWriterFactory(compilerFacade, reflections,
                fallback.getFieldWriterFactory());
        this.fieldReaderFactory = new JavaCompilerFieldReaderFactory(compilerFacade, reflections,
                fallback.getFieldReaderFactory());
    }

    @Override
    public InstanceSupplierFactory getInstanceSupplierFactory() {
        return instanceSupplierFactory;
    }

    @Override
    public FieldWriterFactory getFieldWriterFactory() {
        return fieldWriterFactory;
    }

    @Override
    public FieldReaderFactory getFieldReaderFactory() {
        return fieldReaderFactory;
    }
}
