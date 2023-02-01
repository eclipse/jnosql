/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package org.eclipse.jnosql.mapping.test.jupiter;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.function.Supplier;
import java.util.stream.Stream;

class ContainerSupplier implements Supplier<SeContainer> {

    private final CDIExtension config;

    ContainerSupplier(CDIExtension config) {
        this.config = config;
    }

    @Override
    public SeContainer get() {
        final SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        if (config.disableDiscovery()) {
            initializer.disableDiscovery();
        }
        initializer.setClassLoader(Thread.currentThread().getContextClassLoader());
        initializer.addBeanClasses(config.classes());
        initializer.enableDecorators(config.decorators());
        initializer.enableInterceptors(config.interceptors());
        initializer.selectAlternatives(config.alternatives());
        initializer.selectAlternativeStereotypes(config.alternativeStereotypes());
        initializer.addPackages(getPackages(config.packages()));
        initializer.addPackages(true, getPackages(config.recursivePackages()));
        return initializer.initialize();
    }

    private Package[] getPackages(Class<?>[] packages) {
        return Stream.of(packages).map(Class::getPackage).toArray(Package[]::new);
    }


}
