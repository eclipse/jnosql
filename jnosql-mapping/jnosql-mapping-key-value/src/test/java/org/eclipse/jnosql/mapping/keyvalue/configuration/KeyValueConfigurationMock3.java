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
package org.eclipse.jnosql.mapping.keyvalue.configuration;

import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.communication.keyvalue.BucketManagerFactory;
import org.eclipse.jnosql.communication.keyvalue.KeyValueConfiguration;
import org.eclipse.jnosql.communication.keyvalue.KeyValueEntity;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class KeyValueConfigurationMock3 implements KeyValueConfiguration {


    static final BucketManagerFactory FACTORY = Mockito.mock(BucketManagerFactory.class);

    static final BucketManager MANAGER = Mockito.mock(BucketManager.class);

    static {
        Mockito.when(FACTORY.apply(Mockito.anyString())).thenReturn(MANAGER);
    }

    @Override
    public BucketManagerFactory apply(Settings settings) {
        return FACTORY;
    }





}
