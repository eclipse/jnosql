/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.data;

import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.query.ConditionQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.Where;
import org.eclipse.jnosql.query.grammar.data.JDQLParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.eclipse.jnosql.communication.Condition.NOT;
import static org.eclipse.jnosql.communication.Condition.OR;

abstract class AbstractWhere extends AbstractJDQLProvider {

    protected Where where;

    protected QueryCondition condition;

    protected boolean and = true;

    protected boolean negation = false;

    protected String entity;

    @Override
    protected void runQuery(String query) {
        super.runQuery(query);

        if (Objects.nonNull(condition)) {
            this.where = Where.of(condition);
        }
    }

    @Override
    public void exitFrom_clause(JDQLParser.From_clauseContext ctx) {
        this.entity = ctx.entity_name().getText();
    }

    @Override
    public void exitComparison_expression(JDQLParser.Comparison_expressionContext ctx) {
        super.exitComparison_expression(ctx);
        boolean hasNot = Objects.nonNull(ctx.NEQ());
        var contexts = ctx.scalar_expression();
        var condition = getCondition(ctx);
        var name = contexts.get(0).getText();
        var value = contexts.get(1);
        var literal = PrimaryFunction.INSTANCE.apply(value.primary_expression());
        checkCondition(new DefaultQueryCondition(name, condition, literal), hasNot);
    }

    private Condition getCondition(JDQLParser.Comparison_expressionContext ctx) {
        if (ctx.EQ() != null) {
            return EQUALS;
        } else if (ctx.LT() != null) {
            return Condition.LESSER_THAN;
        } else if (ctx.LTEQ() != null) {
            return Condition.LESSER_EQUALS_THAN;
        } else if (ctx.GT() != null) {
            return Condition.GREATER_THAN;
        } else if (ctx.GTEQ() != null) {
            return Condition.GREATER_EQUALS_THAN;
        }
        throw new UnsupportedOperationException("The operation does not support: " + ctx.getText());
    }

    private void checkCondition(QueryCondition condition, boolean hasNot) {
        QueryCondition newCondition = checkNotCondition(condition, hasNot);
        if (Objects.isNull(this.condition)) {
            this.condition = newCondition;
            return;
        }
        if (and) {
            appendCondition(AND, newCondition);
        } else {
            appendCondition(OR, newCondition);
        }

    }

    private void appendCondition(Condition operator, QueryCondition newCondition) {

        if (operator.equals(this.condition.condition())) {
            ConditionQueryValue conditionValue = ConditionQueryValue.class.cast(this.condition.value());
            List<QueryCondition> conditions = new ArrayList<>(conditionValue.get());
            conditions.add(newCondition);
            this.condition = new DefaultQueryCondition("_" + operator.name(), operator, ConditionQueryValue.of(conditions));
        } else if (isNotAppendable()) {
            List<QueryCondition> conditions = Arrays.asList(this.condition, newCondition);
            this.condition = new DefaultQueryCondition("_" + operator.name(), operator, ConditionQueryValue.of(conditions));
        } else {
            List<QueryCondition> conditions = ConditionQueryValue.class.cast(this.condition.value()).get();
            QueryCondition lastCondition = conditions.get(conditions.size() - 1);

            if (isAppendable(lastCondition) && operator.equals(lastCondition.condition())) {
                List<QueryCondition> lastConditions = new ArrayList<>(ConditionQueryValue.class
                        .cast(lastCondition.value()).get());
                lastConditions.add(newCondition);

                QueryCondition newAppendable = new DefaultQueryCondition("_" + operator.name(),
                        operator, ConditionQueryValue.of(lastConditions));

                List<QueryCondition> newConditions = new ArrayList<>(conditions.subList(0, conditions.size() - 1));
                newConditions.add(newAppendable);
                this.condition = new DefaultQueryCondition(this.condition.name(), this.condition.condition(),
                        ConditionQueryValue.of(newConditions));
            } else {
                QueryCondition newAppendable = new DefaultQueryCondition("_" + operator.name(),
                        operator, ConditionQueryValue.of(Collections.singletonList(newCondition)));

                List<QueryCondition> newConditions = new ArrayList<>(conditions);
                newConditions.add(newAppendable);
                this.condition = new DefaultQueryCondition(this.condition.name(), this.condition.condition(),
                        ConditionQueryValue.of(newConditions));
            }

        }
    }

    private boolean isAppendable(QueryCondition condition) {
        return (AND.equals(condition.condition()) || OR.equals(condition.condition()));
    }

    private boolean isNotAppendable() {
        return !isAppendable(this.condition);
    }

    private QueryCondition checkNotCondition(QueryCondition condition, boolean hasNot) {
        if (hasNot) {
            ConditionQueryValue conditions = ConditionQueryValue.of(Collections.singletonList(condition));
            return new DefaultQueryCondition("_NOT", NOT, conditions);
        } else {
            return condition;
        }
    }
}
