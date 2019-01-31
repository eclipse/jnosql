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

import java.util.function.Function;

/**
 * A supplier to {@link UpdateQuery}
 */
public interface UpdateQuerySupplier extends Function<String, UpdateQuery> {

    /**
     * It returns a {@link UpdateQuery} from {@link java.util.ServiceLoader}
     *
     * @return {@link UpdateQuery} instance
     * @throws IllegalStateException when there isn't UpdateQuery from service loader.
     */
    static UpdateQuerySupplier getSupplier() {
        return UpdateQuerySupplierServiceLoader.getInstance();
    }
}
