package org.jnosql.diana.api.column;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

final class ColumnQueryParserServiceAsyncLoader {

    private static final List<ColumnQueryParserAsync> LOADERS;

    static final Optional<ColumnQueryParserAsync> INSTANCE;

    private static final String MESSAGE = "Could not found an implementation of ColumnQueryParserAsync in service loader.";

    static {
        ServiceLoader<ColumnQueryParserAsync> serviceLoader = ServiceLoader.load(ColumnQueryParserAsync.class);
        LOADERS = StreamSupport.stream(serviceLoader.spliterator(), false).collect(toList());
        INSTANCE = LOADERS.stream().findFirst();
    }

    private ColumnQueryParserServiceAsyncLoader() {
    }

    static ColumnQueryParserAsync getInstance() {
        return INSTANCE.orElseThrow(() -> new IllegalStateException(MESSAGE));
    }
}
