/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package org.eclipse.jnosql.communication.semistructured;

import jakarta.data.page.CursoredPage;
import jakarta.data.page.PageRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DatabaseManagerTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private DatabaseManager databaseManager;


    @Test
    void shouldReturnErrorWhenThereIsNotSort(){
        SelectQuery query = SelectQuery.builder().from("person").build();
        PageRequest<?> pageRequest = PageRequest.ofSize(10);
        assertThrows(IllegalArgumentException.class, () -> databaseManager.selectCursor(query, pageRequest));
    }

    @Test
    void shouldReturnPaginationOffSet(){
        SelectQuery query = SelectQuery.select().from("person")
                .orderBy("name").asc().build();

        var entity = CommunicationEntity.of("name");
        entity.add("name", "Ada");
        entity.add("age", 10);
        entity.add("id", UUID.randomUUID().toString());

        var entity2 = CommunicationEntity.of("name");
        entity2.add("name", "Poliana");
        entity2.add("age", 35);
        entity2.add("id", UUID.randomUUID().toString());

        Mockito.when(databaseManager.select(Mockito.any(SelectQuery.class)))
                .thenReturn(Stream.of(entity, entity2));
        CursoredPage<CommunicationEntity> entities = databaseManager.selectCursor(query,
                PageRequest.ofSize(10));

        SoftAssertions.assertSoftly(soft ->{
            PageRequest<CommunicationEntity> pageRequest = entities.pageRequest();
            PageRequest<CommunicationEntity> nextedPageRequest = entities.nextPageRequest();
            PageRequest.Cursor cursor = nextedPageRequest.cursor().orElseThrow();

            soft.assertThat(entities).hasSize(2);
            soft.assertThat(pageRequest.mode())
                    .isEqualTo(PageRequest.Mode.OFFSET);
            soft.assertThat(nextedPageRequest.mode())
                    .isEqualTo(PageRequest.Mode.CURSOR_NEXT);
            soft.assertThat(cursor.elements())
                    .hasSize(1);
            soft.assertThat(cursor.get(0)).isEqualTo("Poliana");

        });
    }

}