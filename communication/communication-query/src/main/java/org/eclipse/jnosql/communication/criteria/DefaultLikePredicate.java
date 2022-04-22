/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 *   Alessandro Mocatelli
 */
package org.eclipse.jnosql.communication.criteria;

import jakarta.nosql.criteria.Predicate;

public class DefaultLikePredicate<T extends Object> extends AbstractPredicate<T> implements Predicate<T> {
    
    private final String pattern;

    public DefaultLikePredicate(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
    
}
