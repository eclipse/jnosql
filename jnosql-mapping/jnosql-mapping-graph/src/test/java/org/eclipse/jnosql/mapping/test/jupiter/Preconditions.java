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

package org.eclipse.jnosql.mapping.graph.org.eclipse.jnosql.mapping.test.jupiter;

import org.junit.platform.commons.PreconditionViolationException;

final class Preconditions {

    private Preconditions() {
    }

    static <T> T notNull(T object, String message) throws PreconditionViolationException {
        condition(object != null, message);
        return object;
    }

    static void condition(boolean predicate, String message) throws PreconditionViolationException {
        if (!predicate) {
            throw new PreconditionViolationException(message);
        }
    }
}
