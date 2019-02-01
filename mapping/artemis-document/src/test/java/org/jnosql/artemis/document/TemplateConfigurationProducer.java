/*
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
 */
package org.jnosql.artemis.document;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@ExtendWith(CDIExtension.class)
public class TemplateConfigurationProducer {


    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentTemplate templateA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2", database = "database")
    private DocumentTemplate templateB;



    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentTemplateAsync templateAsyncA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2", database = "database")
    private DocumentTemplateAsync templateAsyncB;

    @Test
    public void shouldTemplate() {
        Assertions.assertNotNull(templateA);
        DocumentCollectionManager manager = AbstractDocumentTemplate.class.cast(templateA).getManager();
        Assertions.assertNotNull(manager);

    }

    @Test
    public void shouldTemplateB() {
        Assertions.assertNotNull(templateB);
        DocumentCollectionManager manager = AbstractDocumentTemplate.class.cast(templateB).getManager();
        Assertions.assertNotNull(manager);
    }

    @Test
    public void shouldTemplateAsync() {
        Assertions.assertNotNull(templateA);
        DocumentCollectionManagerAsync manager = AbstractDocumentTemplateAsync.class.cast(templateAsyncA).getManager();
        Assertions.assertNotNull(manager);

    }

    @Test
    public void shouldTemplateAsyncB() {
        Assertions.assertNotNull(templateB);
        DocumentCollectionManagerAsync manager = AbstractDocumentTemplateAsync.class.cast(templateAsyncB).getManager();
        Assertions.assertNotNull(manager);
    }

}
