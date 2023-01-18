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
package org.eclipse.jnosql.mapping.validation;


import org.eclipse.jnosql.mapping.EntityPostPersist;
import org.eclipse.jnosql.mapping.EntityPrePersist;
import org.eclipse.jnosql.mapping.reflection.ConstructorEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
class EntityObserver {

    @Inject
    private MappingValidator validator;

    void validate(@Observes EntityPrePersist entity) {
        this.validator.validate(entity.get());
    }

    void validate(@Observes EntityPostPersist entity) {
        this.validator.validate(entity.get());
    }

    void validate(@Observes ConstructorEvent event) {
        this.validator.validate(event);
    }
}
