/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.mongodb.document;


import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.bson.conversions.Bson;

import java.util.List;

final class DocumentQueryConversor {

    private DocumentQueryConversor() {
    }

    public static Bson convert(DocumentCondition condition) {
        Document document = condition.getDocument();
        Object value = document.getValue().get();
        switch (condition.getCondition()) {
            case EQUALS:
                return Filters.eq(document.getName(), value);
            case GREATER_THAN:
                return Filters.gt(document.getName(), value);
            case GREATER_EQUALS_THAN:
                return Filters.gte(document.getName(), value);
            case LESSER_THAN:
                return Filters.lt(document.getName(), value);
            case LESSER_EQUALS_THAN:
                return Filters.lte(document.getName(), value);
            case IN:
                return Filters.in(document.getName(), value);
            case LIKE:
                return Filters.regex(document.getName(), value.toString());
            case AND:
                List<Document> andList = condition.getDocument().getValue().getList(Document.class);
                return Filters.and(andList.stream()
                        .map(d -> new BasicDBObject(d.getName(), d.getValue().get())).toArray(BasicDBObject[]::new));
            case OR:
                List<Document> orList = condition.getDocument().getValue().getList(Document.class);
                return Filters.or(orList.stream()
                        .map(d -> new BasicDBObject(d.getName(), d.getValue().get())).toArray(BasicDBObject[]::new));
            default:
                throw new UnsupportedOperationException("The condition " + condition.getCondition()
                        + " is not supported from mongoDB diana driver" );
        }
    }
}
