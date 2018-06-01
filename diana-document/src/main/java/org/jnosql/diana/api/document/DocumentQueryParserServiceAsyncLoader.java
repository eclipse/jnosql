package org.jnosql.diana.api.document;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

final class DocumentQueryParserServiceAsyncLoader {

    private static final List<DocumentQueryParserAsync> LOADERS;

    static final Optional<DocumentQueryParserAsync> INSTANCE;

    private static final String MESSAGE = "Could not found an implementation of DocumentQueryParserAsync in service loader.";

    static {
        ServiceLoader<DocumentQueryParserAsync> serviceLoader = ServiceLoader.load(DocumentQueryParserAsync.class);
        LOADERS = StreamSupport.stream(serviceLoader.spliterator(), false).collect(toList());
        INSTANCE = LOADERS.stream().findFirst();
    }

    private DocumentQueryParserServiceAsyncLoader() {
    }

    static DocumentQueryParserAsync getInstance() {
        return INSTANCE.orElseThrow(() -> new IllegalStateException(MESSAGE));
    }
}
