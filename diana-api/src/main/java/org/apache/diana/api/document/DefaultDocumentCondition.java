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
