/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.api.document.query.parser;

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentQueryParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DefaultDocumentQueryParserTest {

    private DocumentQueryParser parser = new DefaultDocumentQueryParser();



    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {

        DocumentCollectionManager documentCollection = Mocki;
        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query(null, documentCollection);
        });
    }


}