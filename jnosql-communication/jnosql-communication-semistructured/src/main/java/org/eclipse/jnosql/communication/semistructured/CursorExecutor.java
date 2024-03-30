/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.communication.semistructured;

import jakarta.data.Sort;
import jakarta.data.page.CursoredPage;
import jakarta.data.page.PageRequest;
import jakarta.data.page.impl.CursoredPageRecord;
import org.eclipse.jnosql.communication.CommunicationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum CursorExecutor {

    OFF_SET {
        @SuppressWarnings("unchecked")
        @Override
        public CursoredPage<CommunicationEntity> cursor(SelectQuery query, PageRequest<?> pageRequest, DatabaseManager template) {

            var select = new DefaultSelectQuery(pageRequest.size(), 0, query.name(), query.columns(), query.sorts(),
                    query.condition().orElse(null));

            var entities = template.select(select).toList();
            var last = entities.isEmpty() ? null : entities.get(entities.size() - 1);
            if (last == null) {
                return new CursoredPageRecord<>(entities, Collections.emptyList(), -1, (PageRequest<CommunicationEntity>) pageRequest,
                        null, null);
            } else {
                PageRequest.Cursor cursor = getCursor(query.sorts(), last);
                PageRequest<CommunicationEntity> afterCursor = PageRequest.<CommunicationEntity>ofSize(pageRequest.size()).afterCursor(cursor);

                return new CursoredPageRecord<>(entities, Collections.emptyList(), -1, (PageRequest<CommunicationEntity>)
                        pageRequest, afterCursor, null);
            }
        }

        private <T> PageRequest.Cursor getCursor(List<Sort<?>> sorts, CommunicationEntity entity) {
            List<Object> keys = new ArrayList<>();
            for (Sort<?> sort : sorts) {
                String name = sort.property();
                Element element = entity.find(name)
                        .orElseThrow(() -> new CommunicationException("The sort name does not exist in the entity: " + name));
                keys.add(element.get());
            }
            return PageRequest.Cursor.forKey(keys.toArray());
        }
    }, CURSOR_NEXT {
        @Override
        public CursoredPage<CommunicationEntity> cursor(SelectQuery query, PageRequest<?> pageRequest, DatabaseManager template) {
            return null;
        }
    }, CURSOR_PREVIOUS {
        @Override
        public CursoredPage<CommunicationEntity> cursor(SelectQuery query, PageRequest<?> pageRequest, DatabaseManager template) {
            return null;
        }
    };


    abstract CursoredPage<CommunicationEntity> cursor(SelectQuery query, PageRequest<?> pageRequest, DatabaseManager template);

    public static CursorExecutor of(PageRequest.Mode value) {

        return switch (value) {
            case CURSOR_NEXT -> CURSOR_NEXT;
            case CURSOR_PREVIOUS -> CURSOR_PREVIOUS;
            default -> OFF_SET;
        };

    }
}
