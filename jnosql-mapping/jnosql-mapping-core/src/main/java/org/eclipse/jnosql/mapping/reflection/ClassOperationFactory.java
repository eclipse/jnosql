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


import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

enum ClassOperationFactory implements Supplier<ClassOperation> {

    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(ClassOperationFactory.class.getName());

    private final Reflections reflections = new DefaultReflections();

    private final ClassOperation reflection = new ReflectionClassOperation(reflections);

    public Reflections getReflections() {
        return reflections;
    }

    @Override
    public ClassOperation get() {

        LOGGER.info("Logging the operation factory");
        ServiceLoader<ClassOperation> serviceLoader = ServiceLoader.load(ClassOperation.class);

        Optional<ClassOperation> classOperation = StreamSupport.stream(serviceLoader.spliterator(), false)
                .findFirst();

        if (classOperation.isPresent()) {
            ClassOperation operation = classOperation.get();
            LOGGER.info("ClassOperation found: " + operation.getClass());
            return operation;
        } else {
            LOGGER.info("ClassOperation does not found, using the default implementation");
            return reflection;
        }


    }
}
