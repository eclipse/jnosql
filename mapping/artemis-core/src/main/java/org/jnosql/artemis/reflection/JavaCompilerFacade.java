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


import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.security.AccessController.doPrivileged;

/**
 * Class that converts a {@link JavaSource} to a compiled class
 */
final class JavaCompilerFacade {

    private static final Pattern BREAK_LINE = Pattern.compile("\n");
    private final JavaCompilerClassLoader classLoader;
    private final JavaCompiler compiler;
    private final DiagnosticCollector<javax.tools.JavaFileObject> diagnosticCollector;

    public JavaCompilerFacade(ClassLoader loader) {
        this.compiler = Optional.ofNullable(ToolProvider.getSystemJavaCompiler())
                .orElseThrow(() -> new IllegalStateException("Cannot find the system Java compiler"));

        PrivilegedAction<JavaCompilerClassLoader> action = () -> new JavaCompilerClassLoader(loader);
        this.classLoader = doPrivileged(action);
        this.diagnosticCollector = new DiagnosticCollector<>();
    }

    public <T> Class<? extends T> apply(JavaSource<T> source) {
        return compile(source);
    }

    private synchronized <T> Class<? extends T> compile(JavaSource<T> source) {
        JavaFileObject fileObject = new JavaFileObject(source.getSimpleName(), source.getJavaSource());

        JavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);

        try (GeneratedJavaFileManager javaFileManager = new GeneratedJavaFileManager(standardFileManager, classLoader)) {
            CompilationTask task = compiler.getTask(null, javaFileManager, diagnosticCollector,
                    null, null, Collections.singletonList(fileObject));

            if (!task.call()) {
                return createCompilerErrorMessage(source);
            }
        } catch (IOException e) {
            throw new IllegalStateException("The generated class (" + source.getSimpleName() + ") failed to compile because the "
                    + JavaFileManager.class.getSimpleName() + " didn't close.", e);
        }
        try {
            Class<T> compiledClass = (Class<T>) classLoader.loadClass(source.getName());
            if (!source.getType().isAssignableFrom(compiledClass)) {
                throw new ClassCastException("The generated compiledClass (" + compiledClass
                        + ") cannot be assigned to the superclass/interface (" + source.getType() + ").");
            }
            return compiledClass;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("The generated class (" + source.getSimpleName()
                    + ") compiled, but failed to load.", e);
        }

    }

    private <T> Class<? extends T> createCompilerErrorMessage(JavaSource<T> source) {
        String compilationMessages = diagnosticCollector.getDiagnostics().stream()
                .map(d -> d.getKind() + ":[" + d.getLineNumber() + "," + d.getColumnNumber() + "] "
                        + d.getMessage(null)
                        + "\n        " + (d
                        .getLineNumber() <= 0 ? "" : BREAK_LINE.splitAsStream(source.getJavaSource())
                        .skip(d.getLineNumber() - 1).findFirst().orElse("")))
                .collect(Collectors.joining("\n"));
        throw new CompilerAccessException("The generated class (" + source.getSimpleName() + ") failed to compile.\n"
                + compilationMessages);
    }


}