package org.apache.diana.api;


/**
 * This interface represents the converters to be used in Value method,
 * so if there's a new type that the current API doesn't support just creates a new implementation and load it by service load process.
 * @param <T>   - the new type class
 * @author Otávio Santana
 * @see Value
 * @see Value#get(Class)
 */
public interface ReaderField<T> {

    /**
     * verifies if the reader has support of instance from this class.
     *
     * @param clazz - {@link Class} to be verified
     * @return true if the implementation is can support this class, otherwise false
     */
    boolean isCompatible(Class clazz);

    /**
     * Once this implementation is compatible with the class type, the next step it converts  an
     * instance to this new one from the rightful class.
     *
     * @param value - instance to be converted
     * @return a new instance converted from required class
     */
    T read(Object value);

}
