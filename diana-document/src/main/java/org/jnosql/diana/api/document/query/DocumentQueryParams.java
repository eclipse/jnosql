package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.query.Params;

/**
 * The result of {@link SelectQueryConverter} that has {@link DocumentQuery} and {@link org.jnosql.query.Params}.
 */
public interface DocumentQueryParams {

    /**
     * The {@link DocumentQuery}
     *
     * @return a {@link DocumentQuery} instance
     */
    DocumentQuery getQuery();

    /**
     * The {@link org.jnosql.query.Params}
     *
     * @return a {@link org.jnosql.query.Params} instance
     */
    Params getParams();
}
