/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Specifies the value of the discriminator column for entities of the given type.
 *
 * <p> The <code>DiscriminatorValue</code>
 * annotation can only be specified on a concrete entity
 * class.
 *
 * <p> If the <code>DiscriminatorValue</code> annotation is not
 * specified and a discriminator column is used, a provider-specific
 * function will be used to generate a value representing the
 * entity type. So the discriminator value default is the {@link Class#getSimpleName()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiscriminatorValue {
    /**
     * (Optional) The value that indicates that the
     * row is an entity of the annotated entity type.
     * @return the discriminator Value
     */
    String value();
}
