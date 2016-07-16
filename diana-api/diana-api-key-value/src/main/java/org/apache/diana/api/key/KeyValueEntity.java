package org.apache.diana.api.key;




import java.io.Serializable;
import java.util.Optional;

public interface KeyValueEntity {

    <T extends Serializable> void put(String key, T value);

    <T extends Serializable> Optional<T> get(String key, Class<T> entityClass);

    <T extends Serializable> Iterable<T> get(Iterable<String> key, Class<T> entityClass);

    void remove(String key);

    void remove(Iterable<String> keys);

}
