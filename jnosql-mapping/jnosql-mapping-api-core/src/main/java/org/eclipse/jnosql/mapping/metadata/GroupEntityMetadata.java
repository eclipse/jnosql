package org.eclipse.jnosql.mapping.metadata;

import java.util.Map;

/**
 * This interface represents metadata for grouped entities in the context of a data mapping framework.
 * It provides methods to access mappings and classes related to entity metadata.
 *
 * <p>The {@code GroupEntityMetadata} interface defines methods for retrieving information about
 * loaded entity mappings and classes. It is often used to manage and access metadata information
 * for multiple entities in a data mapping framework.
 */
public interface GroupEntityMetadata {

    /**
     * Returns a mapping of entity names to their corresponding {@link EntityMetadata}.
     *
     * @return A map containing entity names and their corresponding metadata.
     */
    Map<String, EntityMetadata> getMappings();

    /**
     * Returns a mapping of Java classes to their corresponding {@link EntityMetadata}.
     *
     * @return A map containing Java classes and their corresponding metadata.
     */
    Map<Class<?>, EntityMetadata> getClasses();
}