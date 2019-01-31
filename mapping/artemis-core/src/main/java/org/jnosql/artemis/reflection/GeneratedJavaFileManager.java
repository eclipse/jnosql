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

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

final class GeneratedJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final JavaCompilerClassLoader classLoader;

    public GeneratedJavaFileManager(JavaFileManager fileManager, JavaCompilerClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject sibling) {
        if (kind != Kind.CLASS) {
            throw new IllegalArgumentException("Unsupported kind (" + kind + ") for class (" + qualifiedName + ").");
        }
        JavaCompiledStream fileObject = new JavaCompiledStream(qualifiedName);
        classLoader.addJavaFileObject(qualifiedName, fileObject);
        return fileObject;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

}