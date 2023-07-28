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
 * Specifies the discriminator column for the mapping strategy.
 * If the <code>DiscriminatorColumn</code> annotation is missing,
 * the name of the discriminator column defaults is <code>"dtype"</code>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiscriminatorColumn {

    String DEFAULT_DISCRIMINATOR_COLUMN = "dtype";
    /**
     * (Optional) The name of column to be used for the discriminator.
     * @return the column's name
     */
    String value() default DEFAULT_DISCRIMINATOR_COLUMN;
}
