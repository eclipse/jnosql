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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

final class JavaCompiledStream extends SimpleJavaFileObject {

    private ByteArrayOutputStream classOutputStream;

    public JavaCompiledStream(String fullClassName) {
        super(URI.create("bytes:///" + fullClassName), Kind.CLASS);
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(getClassBytes());
    }

    @Override
    public OutputStream openOutputStream() {
        classOutputStream = new ByteArrayOutputStream();
        return classOutputStream;
    }

    public byte[] getClassBytes() {
        return classOutputStream.toByteArray();
    }

}