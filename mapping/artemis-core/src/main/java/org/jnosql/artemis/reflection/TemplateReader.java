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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

enum TemplateReader implements Function<String, String> {

    INSTANCE;

    public static final int BUFFER = 1024;

    @Override
    public String apply(String file) {
        ClassLoader classLoader = TemplateReader.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[BUFFER];
        try {
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            byte[] byteArray = buffer.toByteArray();
            return new String(byteArray, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new TemplateReaderException("An error to load from the file: " + file, ex);
        }
    }

    class TemplateReaderException extends RuntimeException {

        public TemplateReaderException(String message, IOException ex) {
            super(message, ex);
        }
    }
}

