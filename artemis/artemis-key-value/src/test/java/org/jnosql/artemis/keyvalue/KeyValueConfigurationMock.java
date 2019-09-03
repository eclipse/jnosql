/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.keyvalue;

import jakarta.nosql.Settings;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.BucketManagerFactory;
import jakarta.nosql.keyvalue.KeyValueConfiguration;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class KeyValueConfigurationMock implements KeyValueConfiguration {


    @Override
    public BucketManagerFactory get() {
        return null;
    }

    @Override
    public BucketManagerFactory get(Settings settings) {
        return new BucketManagerFactoryMock(settings);
    }


    public static class BucketManagerFactoryMock implements BucketManagerFactory {

        private final Settings settings;

        public BucketManagerFactoryMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public BucketManager getBucketManager(String bucketName) throws UnsupportedOperationException, NullPointerException {
            return Mockito.mock(BucketManager.class);
        }

        @Override
        public void close() {

        }

        @Override
        public Map getMap(String bucketName, Class keyValue, Class valueValue) throws UnsupportedOperationException, NullPointerException {
            return null;
        }

        @Override
        public Queue getQueue(String bucketName, Class clazz) throws UnsupportedOperationException, NullPointerException {
            return null;
        }

        @Override
        public Set getSet(String bucketName, Class clazz) throws UnsupportedOperationException, NullPointerException {
            return null;
        }

        @Override
        public List getList(String bucketName, Class clazz) throws UnsupportedOperationException, NullPointerException {
            return null;
        }
    }


}
