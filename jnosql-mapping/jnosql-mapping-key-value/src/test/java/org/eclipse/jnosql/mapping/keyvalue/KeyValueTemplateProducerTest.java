/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.inject.Inject;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableAutoWeld
@AddPackages(value = {Converters.class, KeyValueEntityConverter.class})
@AddPackages(MockProducer.class)
@AddExtensions({EntityMetadataExtension.class, KeyValueExtension.class})
public class KeyValueTemplateProducerTest {


    @Inject
    private KeyValueTemplateProducer producer;


    @Test
    public void shouldReturnErrorWhenManagerNull() {
        assertThrows(NullPointerException.class, () -> producer.apply(null));
    }

    @Test
    public void shouldReturn() {
        BucketManager manager = Mockito.mock(BucketManager.class);
        KeyValueTemplate repository = producer.apply(manager);
        assertNotNull(repository);
    }
}
