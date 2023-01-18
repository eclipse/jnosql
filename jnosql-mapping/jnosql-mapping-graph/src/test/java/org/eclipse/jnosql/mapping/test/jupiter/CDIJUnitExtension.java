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

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionTarget;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

class CDIJUnitExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private SeContainer container;
    private CreationalContext<Object> context;

    @Override
    public void beforeAll(final ExtensionContext extensionContext) {
        final org.eclipse.jnosql.mapping.test.jupiter.CDIExtension config = org.eclipse.jnosql.mapping.test.jupiter.AnnotationUtils.findAnnotation(extensionContext.getElement(), CDIExtension.class)
                .orElse(null);
        if (config == null) {
            return;
        }
        Supplier<SeContainer> supplier = new org.eclipse.jnosql.mapping.test.jupiter.ContainerSupplier(config);
        container = supplier.get();
    }

    @Override
    public void afterAll(final ExtensionContext extensionContext) {
        if (container != null) {
            doClose(container);
            container = null;
        }
    }

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        if (container == null) {
            return;
        }
        extensionContext.getTestInstance().ifPresent(inject());
    }

    private Consumer<Object> inject() {
        return instance ->  {
            final BeanManager manager = container.getBeanManager();
            final AnnotatedType<?> type = manager.createAnnotatedType(instance.getClass());

            final InjectionTarget injectionTarget = manager.getInjectionTargetFactory(type)
                    .createInjectionTarget(null);
            context = manager.createCreationalContext(null);
            injectionTarget.inject(instance, context);
        };
    }

    @Override
    public void afterEach(final ExtensionContext extensionContext) {
        if (context != null) {
            context.release();
            context = null;
        }
    }

    private void doClose(final SeContainer container) {
        container.close();
    }
}