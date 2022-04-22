/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.metamodel;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCodeWriter extends CodeWriter {
    
    private final OutputStream outputStream;
    
    public OutputStreamCodeWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public OutputStream openBinary(JPackage jp, String string) throws IOException {
        return this.outputStream;
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
    
}
