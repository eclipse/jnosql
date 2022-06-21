/*
 *  Copyright (c) 2020 Otávio Santana and others
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
open module org.eclipse.jnosql.mapping.key.value {
    requires jakarta.nosql.communication.key.value;
    requires jakarta.nosql.mapping.core;
    requires jakarta.nosql.communication.core;
    requires jakarta.nosql.mapping.key.value;
    requires javax.inject;
    requires jakarta.enterprise.cdi.api;
    requires java.logging;
    requires org.eclipse.jnosql.mapping.core;
    requires microprofile.config.api;

    provides javax.enterprise.inject.spi.Extension with org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
    provides org.eclipse.microprofile.config.spi.Converter with org.eclipse.jnosql.mapping.keyvalue.configuration.BucketManagerConverter,
            org.eclipse.jnosql.mapping.keyvalue.configuration.BucketManagerFactoryConverter,
            org.eclipse.jnosql.mapping.keyvalue.configuration.KeyValueTemplateConverter;
}