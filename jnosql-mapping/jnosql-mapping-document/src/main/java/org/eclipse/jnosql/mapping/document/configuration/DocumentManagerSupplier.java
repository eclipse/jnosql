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
package org.eclipse.jnosql.mapping.document.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.config.MicroProfileSettings;
import org.eclipse.jnosql.mapping.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import java.util.Optional;
import java.util.function.Supplier;

import static org.eclipse.jnosql.mapping.config.MappingConfigurations.DOCUMENT_DATABASE;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.DOCUMENT_PROVIDER;

@ApplicationScoped
class DocumentManagerSupplier implements Supplier<DocumentCollectionManager> {

    @Override
    @Produces
    @ApplicationScoped
    public DocumentCollectionManager get() {
        Settings settings = MicroProfileSettings.INSTANCE;

        DocumentConfiguration configuration = settings.get(DOCUMENT_PROVIDER, Class.class)
                .filter(c -> DocumentConfiguration.class.isAssignableFrom(c))
                .map(c -> {
                    final Reflections reflections = CDI.current().select(Reflections.class).get();
                    return (DocumentConfiguration) reflections.newInstance(c);
                }).orElseGet(() -> DocumentConfiguration.getConfiguration());

        DocumentCollectionManagerFactory managerFactory = configuration.get(settings);

        Optional<String> database = settings.get(DOCUMENT_DATABASE, String.class);
        String db = database.orElseThrow(() -> new MappingException("Please, inform the database filling up the property "
                + DOCUMENT_DATABASE));
        DocumentCollectionManager manager = managerFactory.get(db);
        return manager;
    }

    public void close(@Disposes DocumentCollectionManager manager) {
        manager.close();
    }
}
