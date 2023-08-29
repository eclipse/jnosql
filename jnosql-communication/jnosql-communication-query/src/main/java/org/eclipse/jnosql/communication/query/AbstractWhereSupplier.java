/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.query.grammar.QueryParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.BETWEEN;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.eclipse.jnosql.communication.Condition.GREATER_EQUALS_THAN;
import static org.eclipse.jnosql.communication.Condition.GREATER_THAN;
import static org.eclipse.jnosql.communication.Condition.IN;
import static org.eclipse.jnosql.communication.Condition.LESSER_EQUALS_THAN;
import static org.eclipse.jnosql.communication.Condition.LESSER_THAN;
import static org.eclipse.jnosql.communication.Condition.LIKE;
import static org.eclipse.jnosql.communication.Condition.NOT;
import static org.eclipse.jnosql.communication.Condition.OR;

abstract class AbstractWhereSupplier extends AbstractSupplier {

    protected Where where;

    protected QueryCondition condition;

    protected boolean and = true;


    @Override
    protected void runQuery(String query) {
        super.runQuery(query);
        if (Objects.nonNull(condition)) {
            this.where = new Where(condition);
        }
    }

    @Override
    public void exitEq(QueryParser.EqContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?> value = ValueConverter.get(ctx.value());
        checkCondition(new DefaultQueryCondition(name, EQUALS, value), hasNot);
    }

    @Override
    public void exitLt(QueryParser.LtContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?> value = ValueConverter.get(ctx.value());
        checkCondition(new DefaultQueryCondition(name, LESSER_THAN, value), hasNot);
    }

    @Override
    public void exitLte(QueryParser.LteContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?> value = ValueConverter.get(ctx.value());
        checkCondition(new DefaultQueryCondition(name, LESSER_EQUALS_THAN, value), hasNot);
    }

    @Override
    public void exitGt(QueryParser.GtContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?> value = ValueConverter.get(ctx.value());
        checkCondition(new DefaultQueryCondition(name, GREATER_THAN, value), hasNot);
    }

    @Override
    public void exitGte(QueryParser.GteContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?> value = ValueConverter.get(ctx.value());
        checkCondition(new DefaultQueryCondition(name, GREATER_EQUALS_THAN, value), hasNot);
    }

    @Override
    public void exitIn(QueryParser.InContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?>[] values = ctx.value().stream()
                .map(ValueConverter::get)
                .toArray(QueryValue[]::new);
        DefaultArrayQueryValue value = DefaultArrayQueryValue.of(values);
        checkCondition(new DefaultQueryCondition(name, IN, value), hasNot);
    }


    @Override
    public void exitLike(QueryParser.LikeContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<String> value = StringParameterConverter.get(ctx.string_parameter());
        checkCondition(new DefaultQueryCondition(name, LIKE, value), hasNot);
    }

    @Override
    public void exitBetween(QueryParser.BetweenContext ctx) {
        boolean hasNot = Objects.nonNull(ctx.not());
        String name = ctx.name().getText();
        QueryValue<?>[] values = ctx.value().stream().map(ValueConverter::get).toArray(QueryValue[]::new);
        checkCondition(new DefaultQueryCondition(name, BETWEEN, DefaultArrayQueryValue.of(values)), hasNot);
    }

    @Override
    public void exitAnd(QueryParser.AndContext ctx) {
        this.and = true;
    }

    @Override
    public void exitOr(QueryParser.OrContext ctx) {
        this.and = false;
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
