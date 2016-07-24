package org.apache.diana.mongodb.document;


import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import org.apache.diana.api.document.Document;
import org.apache.diana.api.document.DocumentCondition;
import org.bson.conversions.Bson;

import java.util.List;

final class DocumentQueryConversor {

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
                return Filters.and(andList.stream().map(d -> new BasicDBObject(d.getName(), d.getValue().get())).toArray(BasicDBObject[]::new));
            case OR:
                List<Document> orList = condition.getDocument().getValue().getList(Document.class);
                return Filters.or(orList.stream().map(d -> new BasicDBObject(d.getName(), d.getValue().get())).toArray(BasicDBObject[]::new));
            default:
                throw new UnsupportedOperationException("The condition " + condition.getCondition()+ " is not supported from mongoDB diana driver" );
        }
    }
}
