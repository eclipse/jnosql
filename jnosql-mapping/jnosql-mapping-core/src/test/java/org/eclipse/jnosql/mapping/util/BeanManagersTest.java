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
package org.eclipse.jnosql.mapping.util;

import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class BeanManagersTest {

    @Inject
    private BeanManager beanManager;

    @Test
    public void shouldInstance() {
        final ClassMappings classMappings = BeanManagers.getInstance(ClassMappings.class, beanManager);
        assertNotNull(classMappings);
    }

    @Test
    public void shouldInstanceWithQualifier() {
        final ClassMappings classMappings = BeanManagers.getInstance(ClassMappings.class
                , new Default.Literal(), beanManager);
        assertNotNull(classMappings);
    }

    @Test
    public void shouldReturnBeanManager() {
        final BeanManager beanManager = BeanManagers.getBeanManager();
        assertNotNull(beanManager);
    }

    @Test
    public void shouldInstanceWithCurrentBeanManager() {
        final ClassMappings classMappings = BeanManagers.getInstance(ClassMappings.class);
        assertNotNull(classMappings);
    }

    @Test
    public void shouldInstanceWithQualifierWithCurrentBeanManager() {
        final ClassMappings classMappings = BeanManagers.getInstance(ClassMappings.class
                , new Default.Literal());
        assertNotNull(classMappings);
    }

}