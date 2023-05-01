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
package org.eclipse.jnosql.mapping.document.spi;


import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.nosql.document.DocumentTemplate;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.document.DocumentTemplateProducer;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.spi.AbstractBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class TemplateBean extends AbstractBean<JNoSQLDocumentTemplate> {

    private static final Set<Type> TYPES = Set.of(DocumentTemplate.class, JNoSQLDocumentTemplate.class);

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param provider    the provider name, that must be a
     */
    public TemplateBean( String provider) {
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofDocument(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return DocumentTemplate.class;
    }


    @Override
    public JNoSQLDocumentTemplate create(CreationalContext<JNoSQLDocumentTemplate> context) {

        DocumentTemplateProducer producer = getInstance(DocumentTemplateProducer.class);
        DocumentManager manager = getManager();
        return producer.get(manager);
    }

    private DocumentManager getManager() {
        return getInstance(DocumentManager.class, DatabaseQualifier.ofDocument(provider));
    }

    @Override
    public Set<Type> getTypes() {
        return TYPES;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public String getId() {
        return DocumentTemplate.class.getName() + DatabaseType.DOCUMENT + "-" + provider;
    }

}
