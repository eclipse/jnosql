package org.apache.diana.api.document;


import org.apache.diana.api.Condition;

import static org.apache.diana.api.Condition.*;

/**
 * An unit condition  to run a document collection query
 *
 * @author Otávio Santana
 * @see DocumentCollectionManager#find(DocumentQuery)
 */
public interface DocumentCondition {

    /**
     * Gets the document to be used in the query
     *
     * @return a document instance
     */
    Document getDocument();

    /**
     * Gets the conditions to be used in the query
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#EQUALS}, it means a query will scanning to a
     * document collection that has the same name and equals value informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition eq(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, EQUALS);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_THAN}, it means a query will scanning to a
     * document collection that has the same name and the value  greater than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition gt(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, GREATER_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_EQUALS_THAN}, it means a query will scanning to a
     * document collection that has the same name and the value  greater or equals than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition gte(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_THAN}, it means a query will scanning to a
     * document collection that has the same name and the value  lesser than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition lt(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LESSER_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_EQUALS_THAN}, it means a query will scanning to a
     * document collection that has the same name and the value  lesser or equals than informed in this document.
     *
     * @param document a document instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition lte(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#IN}, it means a query will scanning to a
     * document collection that has the same name and the value is within informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition in(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, IN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LIKE}, it means a query will scanning to a
     * document collection that has the same name and the value  is like than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition like(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LIKE);
    }

}
