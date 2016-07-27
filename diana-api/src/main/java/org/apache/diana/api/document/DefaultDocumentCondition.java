package org.apache.diana.api.document;


import org.apache.diana.api.Condition;

import java.util.Objects;

/**
 * The default implementation of {@link DocumentCondition}
 * @author Otávio Santana
 */
class DefaultDocumentCondition implements DocumentCondition {

    private final Document document;

    private final Condition condition;

    private DefaultDocumentCondition(Document document, Condition condition) {
        this.document = document;
        this.condition = condition;
    }

    public static DefaultDocumentCondition of(Document document, Condition condition) {
        return new DefaultDocumentCondition(Objects.requireNonNull(document,"Document is required") , condition);
    }

    public Document getDocument() {
        return document;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultDocumentCondition that = (DefaultDocumentCondition) o;
        return Objects.equals(document, that.document) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentCondition{");
        sb.append("document=").append(document);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
