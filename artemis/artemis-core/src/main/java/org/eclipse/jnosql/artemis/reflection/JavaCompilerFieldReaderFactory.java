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

import jakarta.nosql.mapping.reflection.FieldReader;
import jakarta.nosql.mapping.reflection.FieldReaderFactory;
import jakarta.nosql.mapping.reflection.Reflections;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An {@link FieldReaderFactory} implementation that compiles the code and creates/compile Java code
 * that uses standard Java convention the getter accessor, otherwise
 * it will use a fallback by reflection.
 */
final class JavaCompilerFieldReaderFactory implements FieldReaderFactory {

    private static final Logger LOGGER = Logger.getLogger(JavaCompilerFieldReaderFactory.class.getName());

    private static final String TEMPLATE_FILE = "FieldReader.tempalte";

    private static final String TEMPLATE = TemplateReader.INSTANCE.apply(TEMPLATE_FILE);

    private final JavaCompilerFacade compilerFacade;

    private final Reflections reflections;

    private final FieldReaderFactory fallback;

    JavaCompilerFieldReaderFactory(JavaCompilerFacade compilerFacade, Reflections reflections, FieldReaderFactory fallback) {
        this.compilerFacade = compilerFacade;
        this.reflections = reflections;
        this.fallback = fallback;
    }


    @Override
    public FieldReader apply(Field field) {

        Class<?> declaringClass = field.getDeclaringClass();
        Optional<String> methodName = getMethodName(declaringClass, field);

        return methodName.map(compile(declaringClass))
                .orElseGet(() -> fallback.apply(field));

    }

    private Function<String, FieldReader> compile(Class<?> declaringClass) {
        return method -> {
            String packageName = declaringClass.getPackage().getName();

            String simpleName = declaringClass.getSimpleName() + "$" + method;
            String newInstance = declaringClass.getName();
            String name = declaringClass.getName() + "$" + method;
            String javaSource = StringFormatter.INSTANCE.format(TEMPLATE, packageName, simpleName, newInstance, method);
            FieldReaderJavaSource source = new FieldReaderJavaSource(name, simpleName, javaSource);
            Optional<Class<? extends FieldReader>> reader = compilerFacade.apply(source);
            return reader.map(reflections::newInstance).orElse(null);
        };
    }

    private Optional<String> getMethodName(Class<?> declaringClass, Field field) {
        try {
            Method readMethod = new PropertyDescriptor(field.getName(), declaringClass).getReadMethod();
            if (Modifier.isPublic(readMethod.getModifiers())) {
                return Optional.of(readMethod.getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "A getter method does not exist to the field: "
                    + field.getName() + " within class " + declaringClass.getName() + " using the fallback with reflection", e);

        }
        return Optional.empty();
    }

    private static final class FieldReaderJavaSource implements JavaSource<FieldReader> {

        private final String name;

        private final String simpleName;

        private final String javaSource;


        FieldReaderJavaSource(String name, String simpleName, String javaSource) {
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
        public Class<FieldReader> getType() {
            return FieldReader.class;
        }
    }
}
