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

import jakarta.nosql.mapping.reflection.FieldWriter;
import jakarta.nosql.mapping.reflection.FieldWriterFactory;
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
 * An {@link FieldWriterFactory} implementation that compiles the code and creates/compile
 * Java code that uses standard Java convention the setter accessor,
 * otherwise it will use a fallback by reflection.
 */
final class JavaCompilerFieldWriterFactory implements FieldWriterFactory {

    private static final Logger LOGGER = Logger.getLogger(JavaCompilerFieldWriterFactory.class.getName());

    private static final String TEMPLATE_FILE = "FieldWriter.template";

    private static final String TEMPLATE = TemplateReader.INSTANCE.apply(TEMPLATE_FILE);

    private final JavaCompilerFacade compilerFacade;

    private final Reflections reflections;

    private final FieldWriterFactory fallback;

    JavaCompilerFieldWriterFactory(JavaCompilerFacade compilerFacade, Reflections reflections, FieldWriterFactory fallback) {
        this.compilerFacade = compilerFacade;
        this.reflections = reflections;
        this.fallback = fallback;
    }


    @Override
    public FieldWriter apply(Field field) {

        Class<?> declaringClass = field.getDeclaringClass();
        Optional<String> methodName = getMethodName(declaringClass, field);

        return methodName.map(compile(declaringClass, field.getType()))
                .orElseGet(() -> fallback.apply(field));
    }

    private Function<String, FieldWriter> compile(Class<?> declaringClass, Class<?> type) {
        return method -> {
            String packageName = declaringClass.getPackage().getName();
            String simpleName = declaringClass.getSimpleName() + "$" + method;
            String newInstance = declaringClass.getName();
            String name = declaringClass.getName() + "$" + method;
            String typeCast = type.getName();
            String javaSource = StringFormatter.INSTANCE.format(TEMPLATE, packageName, simpleName,
                    newInstance, method, typeCast);

            FieldWriterJavaSource source = new FieldWriterJavaSource(name, simpleName, javaSource);
            Optional<Class<? extends FieldWriter>> reader =  compilerFacade.apply(source);
            return reader.map(reflections::newInstance).orElse(null);
        };
    }

    private Optional<String> getMethodName(Class<?> declaringClass, Field field) {
        try {
            Method writeMethod = new PropertyDescriptor(field.getName(), declaringClass).getWriteMethod();
            if (Modifier.isPublic(writeMethod.getModifiers())) {
                return Optional.of(writeMethod.getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "A setter method does not exist to the field: "
                    + field.getName() + " within class " + declaringClass.getName() + " using the fallback with reflection", e);

        }
        return Optional.empty();
    }

    private static final class FieldWriterJavaSource implements JavaSource<FieldWriter> {

        private final String name;

        private final String simpleName;

        private final String javaSource;


        FieldWriterJavaSource(String name, String simpleName, String javaSource) {
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
        public Class<FieldWriter> getType() {
            return FieldWriter.class;
        }
    }
}
