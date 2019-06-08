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
 * A supplier to {@link SelectQuery}
 */
public interface SelectQuerySupplier extends Function<String, SelectQuery> {

    /**
     * It returns a {@link SelectQuery} from {@link java.util.ServiceLoader}
     *
     * @return {@link SelectQuery} instance
     * @throws IllegalStateException when there isn't SelectQuery from service loader.
     */
    static SelectQuerySupplier getSupplier() {
        return SelectQuerySupplierServiceLoader.getInstance();
    }
}
