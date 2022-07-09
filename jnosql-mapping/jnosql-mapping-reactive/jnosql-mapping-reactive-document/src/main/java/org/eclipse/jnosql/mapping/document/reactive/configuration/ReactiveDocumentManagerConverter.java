/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.document.reactive.configuration;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplateProducer;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

public class ReactiveDocumentManagerConverter  extends AbstractConfiguration<ReactiveDocumentTemplate>
        implements Converter<ReactiveDocumentTemplate> {

    @Override
    protected ReactiveDocumentTemplate success(String value) {
        Config config = CDI.current().select(Config.class).get();
        final DocumentTemplate template = config.getValue(value, DocumentTemplate.class);
        ReactiveDocumentTemplateProducer producer = CDI.current().select(ReactiveDocumentTemplateProducer.class).get();
        return producer.get(template);
    }
}
