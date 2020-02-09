package org.eclipse.jnosql.artemis;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith(CdiExtension.class)
public @interface CDI {
    /**
     * @return classes to deploy.
     */
    Class<?>[] classes() default {};

    /**
     * @return decorators to activate.
     */
    Class<?>[] decorators() default {};

    /**
     * @return interceptors to activate.
     */
    Class<?>[] interceptors() default {};

    /**
     * @return alternatives to activate.
     */
    Class<?>[] alternatives() default {};

    /**
     * @return stereotypes to activate.
     */
    Class<? extends Annotation>[] alternativeStereotypes() default {};

    /**
     * @return packages to deploy.
     */
    Class<?>[] packages() default {};

    /**
     * @return packages to deploy recursively.
     */
    Class<?>[] recursivePackages() default {};

    /**
     * @return if the automatic scanning must be disabled.
     */
    boolean disableDiscovery() default false;

}