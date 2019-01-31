/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.query;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

final class GetQuerySupplierServiceLoader {

    private static final List<GetQuerySupplier> LOADERS;

    static final Optional<GetQuerySupplier> INSTANCE;

    private static final String MESSAGE = "Could not found an implementation of GetQuerySupplier in service loader.";

    static {
        ServiceLoader<GetQuerySupplier> serviceLoader = ServiceLoader.load(GetQuerySupplier.class);
        LOADERS = StreamSupport.stream(serviceLoader.spliterator(), false).collect(toList());
        INSTANCE = LOADERS.stream().findFirst();
    }

    private GetQuerySupplierServiceLoader() {
    }

    static GetQuerySupplier getInstance() {
        return INSTANCE.orElseThrow(() -> new IllegalStateException(MESSAGE));
    }

}
