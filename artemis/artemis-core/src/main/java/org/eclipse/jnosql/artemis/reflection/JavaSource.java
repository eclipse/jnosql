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

/**
 * The Java source code to be compiled.
 *
 * @param <T> the source result type
 */
interface JavaSource<T> {

    /**
     * returns the {@link Class#getSimpleName()} to the class compiled
     *
     * @return the {@link Class#getSimpleName()}
     */
    String getSimpleName();

    /**
     * returns the {@link Class#getName()} to the class compiled
     *
     * @return the {@link Class#getName()}
     */
    String getName();

    /**
     * returns the java source code
     *
     * @return the java source code
     */
    String getJavaSource();

    /**
     * Returns the type class that the code will compile
     *
     * @return the Super class from the source code
     */
    Class<T> getType();
}
