package org.apache.diana.api.document;


import org.apache.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentQuery {

    private final String collection;

    private final List<DocumentCondition> conditions = new ArrayList<>();

    private final List<Sort> sorts = new ArrayList<>();

    private DocumentQuery(String collection) {
        this.collection = Objects.requireNonNull(collection, "column family is required");
    }

    public static DocumentQuery of(String columnFamily) {
        return new DocumentQuery(columnFamily);
    }

    public DocumentQuery addCondition(DocumentCondition condition) {
        this.conditions.add(Objects.requireNonNull(condition, "condition is required"));
        return this;
    }

    public DocumentQuery addSort(Sort sort) {
        this.sorts.add(Objects.requireNonNull(sort, "Sort is required"));
        return this;
    }

    public String getCollection() {
        return collection;
    }

    public List<DocumentCondition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentQuery that = (DocumentQuery) o;
        return Objects.equals(collection, that.collection) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(sorts, that.sorts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, conditions, sorts);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentQuery{");
        sb.append("collection='").append(collection).append('\'');
        sb.append(", conditions=").append(conditions);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
