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
import org.eclipse.jnosql.mapping.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

public class ReactiveDocumentManagerConverter  extends AbstractConfiguration<ReactiveDocumentTemplate>
        implements Converter<ReactiveDocumentTemplate> {

    @Override
    protected ReactiveDocumentTemplate success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final DocumentTemplate template = config.getValue(value, DocumentTemplate.class);
        ReactiveDocumentTemplateProducer producer = BeanManagers.getInstance(ReactiveDocumentTemplateProducer.class);
        return producer.get(template);
    }
}
