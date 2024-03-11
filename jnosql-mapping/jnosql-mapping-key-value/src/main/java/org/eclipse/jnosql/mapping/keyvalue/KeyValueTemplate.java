package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.nosql.Template;
import org.eclipse.jnosql.mapping.PreparedStatement;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@link Template} specialization for Key-Value databases.
 *
 * <p>
 * These databases store data as key-value pairs, where each key represents a unique identifier
 * for a piece of data.
 * </p>
 *
 * <p>
 * This interface provides some methods that accepts queries in a text format to retrieve from the database but,
 * <b>the query syntax belongs to each provider, thus, it is not Jakarta's NoSQL scope to define it.
 * Accordingly, it might vary from implementation and NoSQL provider.</b>
 * </p>
 */
public interface KeyValueTemplate extends Template {
    /**
     * Saves the entity
     *
     * @param entity the entity to be inserted
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException when entity is null
     */
    <T> T put(T entity);

    /**
     * Saves the entity with time to live
     *
     * @param entity the entity to be inserted
     * @param ttl    the defined time to live
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <T> T put(T entity, Duration ttl);

    /**
     * Saves the {@link Iterable} of entities
     *
     * @param entities keys to be inserted
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException when the iterable is null
     */
    default <T> Iterable<T> put(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::put).toList();
    }

    /**
     * Saves the {@link Iterable} of entities with a defined time to live
     *
     * @param entities entities to be insert
     * @param ttl      the time to entity expire
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    default <T> Iterable<T> put(Iterable<T> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(d -> put(d, ttl)).collect(toList());
    }

    /**
     * Finds the Value from a key
     *
     * @param key  the key
     * @param <K>  the key type
     * @param <T>  the entity type
     * @param type the entity class to convert the result
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException when the key is null
     */
    <K, T> Optional<T> get(K key, Class<T> type);

    /**
     * Executes a query database.
     *
     * <p>
     * <b>The query syntax belongs to each provider, thus, it is not Jakarta's NoSQL scope to define it. Accordingly, it might vary from implementation and NoSQL provider.</b>
     * </p>
     *
     * @param query the query
     * @param type  the entity class
     * @param <T>   the entity type
     * @return the result list, if either <b>put</b> or <b>remove</b> it will return empty
     * @throws NullPointerException          when query is null, if the query is <b>get</b> the entity class is required
     * @throws UnsupportedOperationException when the provider does not support query by text
     */
    <T> Stream<T> query(String query, Class<T> type);

    /**
     * Executes a query database then returns as single result
     *
     * <p>
     * <b>The query syntax belongs to each provider, thus, it is not Jakarta's NoSQL scope to define it. Accordingly, it might vary from implementation and NoSQL provider.</b>
     * </p>
     *
     * @param query the query
     * @param type  the entity class
     * @param <T>   the entity type
     * @return the result {@link Optional}, if either <b>put</b> or <b>remove</b> it will return {@link Optional#empty()}
     * @throws NullPointerException          when query is null, if the query is <b>get</b> the entity class is required
     * @throws UnsupportedOperationException when the provider does not support query by text
     */
    <T> Optional<T> getSingleResult(String query, Class<T> type);

    /**
     * Executes a query database and don't return result, e.g.: when the query is either <b>remove</b> or
     * <b>put</b>
     *
     * @param query the query
     * @throws NullPointerException          when query is null
     * @throws UnsupportedOperationException when the provider does not support query by text
     */
    void query(String query);

    /**
     * Executes a query database with {@link PreparedStatement}
     *
     * <p>
     * <b>The query syntax belongs to each provider, thus, it is not Jakarta's NoSQL scope to define it. Accordingly, it might vary from implementation and NoSQL provider.</b>
     * </p>
     *
     * @param query the query
     * @param type  the entity class
     * @param <T>   the entity type
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException          when query is null, if the query is <b>get</b> the entity class is required
     * @throws UnsupportedOperationException when the provider does not support query by text
     */
    <T> PreparedStatement prepare(String query, Class<T> type);

    /**
     * Finds a list of values from keys
     *
     * @param type the entity class
     * @param keys the keys to be used in this query
     * @param <K>  the key type
     * @param <T>  the entity type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K, T> Iterable<T> get(Iterable<K> keys, Class<T> type);

    /**
     * Removes an entity from key
     *
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void delete(K key);

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> void delete(Iterable<K> keys);

}