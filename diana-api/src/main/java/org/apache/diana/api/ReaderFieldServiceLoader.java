package org.apache.diana.api;


import java.util.ServiceLoader;
import java.util.stream.Stream;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

final class ReaderFieldServiceLoader {
    private static ReaderFieldServiceLoader INSTANCE = new ReaderFieldServiceLoader();

    public static ReaderFieldServiceLoader getInstance() {
        return INSTANCE;
    }

    private final ServiceLoader<ReaderField> serviceLoader = ServiceLoader.load(ReaderField.class);


    <T> T convert(Class<T> clazz, Object value) {

        Stream<ReaderField> stream = stream(spliteratorUnknownSize(serviceLoader.iterator(), ORDERED), false);
        ReaderField readerField = stream.filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return readerField.read(clazz, value);
    }
}
