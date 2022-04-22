/*
 *  Copyright (c) 2022 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *g
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.communication.criteria;

import jakarta.nosql.criteria.CriteriaFunction;
import jakarta.nosql.criteria.Root;

public class DefaultRoot<X extends Object> extends DefaultPath<X, X> implements Root<X> {

    public DefaultRoot(Class<X> type) {
        super(type);
    }

    @Override
    public CriteriaFunction<X, X, Number> count() {
        return new DefaultCountFunction<>();
    }
    
}
