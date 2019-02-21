package org.jnosql.artemis.reflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public interface DynamicReturn {

    <T> Class<T> typeClass();

    Method getMethod();

    <T> Optional<T> singleResult();

    <T> List<T> list();


}
