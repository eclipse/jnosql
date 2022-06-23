/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
module org.eclipse.jnosql.communication.document {
    requires jakarta.nosql.communication.core;
    requires org.eclipse.jnosql.communication.core;
    requires jakarta.nosql.communication.document;
    requires jakarta.nosql.communication.query;
    requires java.json.bind;
    requires java.json;

    opens org.eclipse.jnosql.communication.document.query;

    provides jakarta.nosql.document.DeleteQueryConverter with org.eclipse.jnosql.communication.document.query.DeleteQueryParser;
    provides jakarta.nosql.document.Document.DocumentProvider with org.eclipse.jnosql.communication.document.DefaultDocumentProvider;
    provides jakarta.nosql.document.DocumentCondition.DocumentConditionProvider with org.eclipse.jnosql.communication.document.DefaultDocumentConditionProvider;
    provides jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteProvider with org.eclipse.jnosql.communication.document.query.DefaultDocumentDeleteProvider;
    provides jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteQueryBuilderProvider with org.eclipse.jnosql.communication.document.query.DefaultDocumentDeleteQueryBuilderProvider;
    provides jakarta.nosql.document.DocumentEntity.DocumentEntityProvider with org.eclipse.jnosql.communication.document.DefaultDocumentEntityProvider;
    provides jakarta.nosql.document.DocumentQuery.DocumentQueryBuilderProvider with org.eclipse.jnosql.communication.document.query.DefaultDocumentQueryBuilderProvider;
    provides jakarta.nosql.document.DocumentQuery.DocumentSelectProvider with org.eclipse.jnosql.communication.document.query.DefaultDocumentSelectProvider;
    provides jakarta.nosql.document.DocumentQueryParser with org.eclipse.jnosql.communication.document.query.DefaultDocumentQueryParser;
    provides jakarta.nosql.document.SelectQueryConverter with org.eclipse.jnosql.communication.document.query.SelectQueryParser;

}