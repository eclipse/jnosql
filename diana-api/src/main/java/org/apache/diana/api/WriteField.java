package org.apache.diana.api;

/**
 * To put your own Java Structure in NoSQL database is necessary convert it to a supported one.
 * So, the WriterField has the goal to convert to any specific structure type that a database might support.
 * These implementation will loaded by ServiceLoad and a NoSQL implementation will may use it.
 *
 * @author Otávio Santana
 */
public interface WriteField {

    /**
     * verifies if the writer has support of instance from this class.
     *
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */
    boolean isCompatible(Class clazz);

    /**
     * Converts a specific structure to a new one.
     *
     * @param object the instance to be converted
     * @return a new instance with the new class
     */
    Object write(Object object);
}
