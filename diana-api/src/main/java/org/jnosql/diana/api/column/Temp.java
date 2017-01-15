package org.jnosql.diana.api.column;

import org.jnosql.diana.api.Condition;

public class Temp {

    public static void main(String[] args) {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "otavio");
        //find some column where is equals otavio and greate or equals 26 age
        ColumnCondition condition = ColumnCondition.eq(name)
                .and(ColumnCondition.gte(age));
            //the name is not otavio
        ColumnCondition condition1 = ColumnCondition.eq(name).negate();
    }
}
