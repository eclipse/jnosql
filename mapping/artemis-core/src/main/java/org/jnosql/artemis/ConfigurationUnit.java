/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expresses a dependency to a configuration and its associated persistence unit.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Qualifier
public @interface ConfigurationUnit {


    /**
     * The name of the configuration unit as defined in the settings file.
     *
     * @return the unit name
     */
    @Nonbinding
    String name() default "";

    /**
     * the file name that is within the folder. The default value is jnosql.json
     *
     * @return the file name
     */
    @Nonbinding
    String fileName() default "jnosql.json";

    /**
     * It creates both templates and repositories instances using from the respective database.
     * @return The database to templates and repositories classes.
     */
    @Nonbinding
    String database() default "";
}
