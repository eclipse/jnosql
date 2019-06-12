package org.jnosql.diana.document.query;

import jakarta.nosql.document.DocumentDeleteQuery.DocumentDelete;
import jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

public final class DefaultDocumentDeleteProvider implements DocumentDeleteProvider {

    @Override
    public DocumentDelete apply(String[] documents) {
        if(Stream.of(documents).anyMatch(Objects::isNull)) {
            throw new NullPointerException("there is null document in the query");
        }
        return new DefaultDeleteQueryBuilder(Arrays.asList(documents));
    }

    @Override
    public DocumentDelete get() {
        return new DefaultDeleteQueryBuilder(Collections.emptyList());
    }
}
