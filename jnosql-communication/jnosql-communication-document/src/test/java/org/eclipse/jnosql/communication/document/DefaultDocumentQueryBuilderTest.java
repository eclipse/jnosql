/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *
 * Maximillian Arruda
 *
 */

package org.eclipse.jnosql.communication.document;

import jakarta.data.repository.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultDocumentQueryBuilderTest {

    private DefaultDocumentQueryBuilder builder;

    private String newRandomDocument() {
        return UUID.randomUUID().toString();
    }

    private static Sort newRandomSortRef() {
        return Sort.asc(UUID.randomUUID().toString());
    }

    private String newRandomDocumentCollection() {
        return UUID.randomUUID().toString();
    }

    private DocumentCondition newRandomDocumentCondition() {
        return DocumentCondition.eq(UUID.randomUUID().toString().toString(), UUID.randomUUID().toString().toString());
    }

    @BeforeEach
    void before() {
        builder = newBuilder();
    }

    private DefaultDocumentQueryBuilder newBuilder() {
        return new DefaultDocumentQueryBuilder();
    }

    @Test
    void shouldReturnErrorWhenSelectIsCalledWithNullDocument() {
        assertThrows(NullPointerException.class, () -> builder.select((String) null));
    }

    @Test
    void shouldAcceptToSelectOneValidDocument() {
        assertSame(builder, builder.select(newRandomDocument()));
    }

    @Test
    void shouldAcceptToSelectAnEmptyArrayOfDocuments() {
        assertSame(builder, builder.select(new String[]{}));
    }

    @Test
    void shouldReturnErrorWhenSelectIsCalledWithArrayOfDocumentsWithNullElement() {
        assertThrows(NullPointerException.class, () -> builder.select(newRandomDocument(), null, newRandomDocument()));
    }

    @Test
    void shouldAcceptToSelectAnNonEmptyArrayOfDocumentsWithoutNullElement() {
        assertSame(builder, builder.select(newRandomDocument(), newRandomDocument()));
    }

    @Test
    void shouldReturnErrorWhenSortIsCalledWithNullSortRef() {
        assertThrows(NullPointerException.class, () -> builder.sort((Sort) null));
    }

    @Test
    void shouldAcceptToSortOneValidSortReference() {
        assertSame(builder, builder.sort(newRandomSortRef()));
    }

    @Test
    void shouldAcceptToSortAnEmptyArrayOfSortRef() {
        assertSame(builder, builder.sort(new Sort[]{}));
    }

    @Test
    void shouldReturnErrorWhenSortIsCalledWithArrayOfSortRefWithNullElement() {
        assertThrows(NullPointerException.class, () -> builder.sort(newRandomSortRef(), null, newRandomSortRef()));
    }

    @Test
    void shouldAcceptToSortAnNonEmptyArrayOfSortRefWithoutNullElement() {
        assertSame(builder, builder.sort(newRandomSortRef(), newRandomSortRef()));
    }

    @Test
    void shouldReturnErrorWhenFromIsCalledWithNullDocumentCollection() {
        assertThrows(NullPointerException.class, () -> builder.from(null));
    }

    @Test
    void shouldAcceptToCallFromMethodWithValidDocumentCollection() {
        assertSame(builder, builder.from(newRandomDocumentCollection()));
    }

    @Test
    void shouldReturnErrorWhenWhereIsCalledWithNullDocumentCondition() {
        assertThrows(NullPointerException.class, () -> builder.where(null));
    }

    @Test
    void shouldAcceptToCallWhereMethodWithValidDocumentCondition() {
        assertSame(builder, builder.where(newRandomDocumentCondition()));
    }


    @Test
    void shouldReturnErrorWhenSkipIsCalledWithArgumentLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> builder.skip(-1));
    }

    @Test
    void shouldAcceptToCallSkipWithArgumentGreaterThanZero() {
        assertSame(builder, builder.skip(1));
    }

    @Test
    void shouldReturnErrorWhenLimitIsCalledWithArgumentLessThanZero() {
        assertThrows(IllegalArgumentException.class, () -> builder.limit(-1));
    }

    @Test
    void shouldAcceptToCallLimitWithArgumentGreaterThanZero() {
        assertSame(builder, builder.limit(1));
    }

    @Test
    void shouldReturnErrorWhenBuildIsCalledWithoutDocumentCollectionIsNotProvidedPreviously() {
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    @Test
    void shouldAcceptToBuildWhenDocumentCollectionWasDefinedPreviously() {
        DocumentQuery documentQuery = builder.from(newRandomDocumentCollection()).build();
        assertNotNull(documentQuery);
    }

    @Test
    void shouldReturnErrorWhenGetResultIsCalledWithNullDocumentManagerReference() {
        assertThrows(NullPointerException.class, () -> builder.from(newRandomDocumentCollection()).getResult(null));
    }

    @Test
    void shouldAcceptToCallGetResultWithAValidDocumentManager() {

        DocumentManager manager = mock(DocumentManager.class);
        Stream<DocumentEntity> expectedEntities = Stream.empty();
        when(manager.select(any(DocumentQuery.class))).thenReturn(expectedEntities);
        String collection = newRandomDocumentCollection();
        Stream<DocumentEntity> entities = builder.from(collection).getResult(manager);
        assertSame(expectedEntities, entities);
        verify(manager, atLeastOnce()).select(any(DocumentQuery.class));

    }

    @Test
    void shouldReturnErrorWhenGetSingleResultIsCalledWithNullDocumentManagerReference() {
        assertThrows(NullPointerException.class, () -> builder.from(newRandomDocumentCollection()).getSingleResult(null));
    }

    @Test
    void shouldAcceptToCallGetSingleResultWithAValidDocumentManager() {

        DocumentManager manager = mock(DocumentManager.class);
        Optional<DocumentEntity> expectedResult = Optional.empty();
        when(manager.singleResult(any(DocumentQuery.class))).thenReturn(expectedResult);
        String collection = newRandomDocumentCollection();
        Optional<DocumentEntity> result = builder.from(collection).getSingleResult(manager);
        assertSame(expectedResult, result);
        verify(manager, atLeastOnce()).singleResult(any(DocumentQuery.class));

    }


    @Test
    void shouldBeEqualsToYourself() {
        assertEquals(builder, builder);
    }


    @Test
    void shouldBeNotEqualsToAnyOtherInstanceType() {
        assertNotEquals(builder, new Object());
    }

    @Test
    void shouldBeNotEqualsToAnotherBuilderWithDifferentAttributes() {
        var anotherBuilder = newBuilder()
                .select(newRandomDocument())
                .from(newRandomDocumentCollection())
                .sort(newRandomSortRef())
                .skip(1)
                .limit(2);

        assertNotEquals(builder, anotherBuilder);
    }

    @Test
    void shouldBeEqualsToAnotherBuilderInstanceWithSameAttributes() {
        String document = newRandomDocument();
        String documentCollection = newRandomDocumentCollection();
        Sort sort = newRandomSortRef();
        int skip = 1;
        int limit = 2;

        var builder1 = newBuilder()
                .select(document)
                .from(documentCollection)
                .sort(sort)
                .skip(skip)
                .limit(limit);

        var builder2 = newBuilder()
                .select(document)
                .from(documentCollection)
                .sort(sort)
                .skip(skip)
                .limit(limit);

        assertNotSame(builder1,builder2);

        assertEquals(builder1, builder2);
    }

    @Test
    void shouldHashCodeBeDifferentWhenAttributesAreDifferent() {

        var builder1 = newBuilder()
                .select(newRandomDocument());

        var builder2 = newBuilder()
                .select(newRandomDocument());

        assertNotEquals(builder1.hashCode(),builder2.hashCode());

    }

    @Test
    void shouldHashCodeBeEqualsWhenAttributesAreTheSame() {

        String document = newRandomDocument();
        String documentCollection = newRandomDocumentCollection();
        Sort sort = newRandomSortRef();
        int skip = 1;
        int limit = 2;

        var builder1 = newBuilder();
        var builder2 = newBuilder();

        assertEquals(builder1.hashCode(), builder2.hashCode());


        builder1.select(document);
        builder2.select(document);
        assertEquals(builder1.hashCode(), builder2.hashCode());

        builder1.from(documentCollection);
        builder2.from(documentCollection);
        assertEquals(builder1.hashCode(), builder2.hashCode());

        builder1.sort(sort);
        builder2.sort(sort);
        assertEquals(builder1.hashCode(), builder2.hashCode());

        builder1.skip(skip);
        builder2.skip(skip);
        assertEquals(builder1.hashCode(), builder2.hashCode());

        builder1.limit(limit);
        builder2.limit(limit);
        assertEquals(builder1.hashCode(), builder2.hashCode());

    }

}
