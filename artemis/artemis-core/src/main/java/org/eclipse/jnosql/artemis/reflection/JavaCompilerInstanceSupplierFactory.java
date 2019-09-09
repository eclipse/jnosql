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
package org.eclipse.jnosql.artemis.reflection;

import jakarta.nosql.mapping.reflection.InstanceSupplier;
import jakarta.nosql.mapping.reflection.InstanceSupplierFactory;
import jakarta.nosql.mapping.reflection.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * An {@link InstanceSupplierFactory} implementation that uses compiler code
 */
final class JavaCompilerInstanceSupplierFactory implements InstanceSupplierFactory {

    private static final Logger LOGGER = Logger.getLogger(JavaCompilerInstanceSupplierFactory.class.getName());

    private static final String TEMPLATE_FILE = "InstanceSupplier.template";

    private static final String TEMPLATE = TemplateReader.INSTANCE.apply(TEMPLATE_FILE);

    private final JavaCompilerFacade compilerFacade;

    private final Reflections reflections;

    private final InstanceSupplierFactory fallback;

    JavaCompilerInstanceSupplierFactory(JavaCompilerFacade compilerFacade, Reflections reflections, InstanceSupplierFactory fallback) {
        this.compilerFacade = compilerFacade;
        this.reflections = reflections;
        this.fallback = fallback;
    }

    @Override
    public InstanceSupplier apply(Constructor<?> constructor) {
        Class<?> declaringClass = constructor.getDeclaringClass();
        if (Modifier.isPublic(constructor.getModifiers())) {
            String packageName = declaringClass.getPackage().getName();
            String simpleName = declaringClass.getSimpleName() + "$InstanceSupplier";
            String newInstance = declaringClass.getName();
            String name = declaringClass.getName() + "$InstanceSupplier";
            String javaSource = StringFormatter.INSTANCE.format(TEMPLATE, packageName, simpleName, newInstance);
            InstanceJavaSource source = new InstanceJavaSource(name, simpleName, javaSource);
            Optional<Class<? extends InstanceSupplier>> supplier = compilerFacade.apply(source);
            Optional<InstanceSupplier> instanceSupplier = supplier.map(reflections::newInstance);
            return instanceSupplier.orElseGet(() -> fallback.apply(constructor));

        }

        LOGGER.fine(String.format("The constructor to the class %s is not public, using fallback with Reflectioin",
                declaringClass.getName()));
        return fallback.apply(constructor);
    }

    private static final class InstanceJavaSource implements JavaSource<InstanceSupplier> {

        private final String name;

        private final String simpleName;

        private final String javaSource;


        InstanceJavaSource(String name, String simpleName, String javaSource) {
            this.name = name;
            this.simpleName = simpleName;
            this.javaSource = javaSource;
        }

        @Override
        public String getSimpleName() {
            return simpleName;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getJavaSource() {
            return javaSource;
        }

        @Override
        public Class<InstanceSupplier> getType() {
            return InstanceSupplier.class;
        }
    }
}
