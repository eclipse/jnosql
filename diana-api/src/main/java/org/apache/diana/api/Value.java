package org.apache.diana.api;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface Value extends Serializable {


    <T extends Serializable> T get(Class<T> clazz);

    <T extends Serializable> List<T> getList(Class<T> clazz);

    <T extends Serializable> Stream<T> getStream(Class<T> clazz);

    <T extends Serializable> Set<T> getSet(Class<T> clazz);

    <K extends Serializable, V extends Serializable> Map<K, V> getSet(Class<K> keyclass, Class<V> valueclass);

}
