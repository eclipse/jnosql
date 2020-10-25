/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.document.configuration;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateProducer;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.jnosql.mapping.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link DocumentTemplate} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link jakarta.nosql.document.DocumentConfiguration}
 */
public class DocumentTemplateConverter extends AbstractConfiguration<DocumentTemplate>
        implements Converter<DocumentTemplate> {

    @Override
    public DocumentTemplate success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final DocumentCollectionManager manager = config.getValue(value, DocumentCollectionManager.class);
        DocumentTemplateProducer producer = BeanManagers.getInstance(DocumentTemplateProducer.class);

        return producer.get(manager);
    }
}