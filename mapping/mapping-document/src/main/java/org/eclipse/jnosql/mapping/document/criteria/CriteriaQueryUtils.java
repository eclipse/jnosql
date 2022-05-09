/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.mapping.document.criteria;

import jakarta.nosql.Sort;
import jakarta.nosql.criteria.BinaryPredicate;
import jakarta.nosql.criteria.CompositionPredicate;
import jakarta.nosql.criteria.DisjunctionPredicate;
import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.ExpressionQuery;
import jakarta.nosql.criteria.NegationPredicate;
import jakarta.nosql.criteria.Order;
import jakarta.nosql.criteria.Path;
import jakarta.nosql.criteria.Predicate;
import jakarta.nosql.criteria.RangePredicate;
import jakarta.nosql.criteria.Root;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.criteria.SelectQuery;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CriteriaQueryUtils {

    private CriteriaQueryUtils() {
    }

    public static String join(String... values) {
        return String.join(".", values);
    }

    public static String unfold(Path path) {
        Path tmp = path;
        Deque<String> attributes = new ArrayDeque();
        while (Objects.nonNull(tmp) && !(tmp instanceof Root)) {
            attributes.add(
                    tmp.getAttribute().getName()
            );
            tmp = tmp.getParent();
        }
        return join(
                attributes.stream().filter(
                        value -> !Objects.equals(
                                0,
                                value.trim().length()
                        )
                ).toArray(String[]::new)
        );
    }

    public static String unfold(Expression expression) {
        return join(
                Arrays.asList(
                        unfold(
                                expression.getPath()
                        ),
                        expression.getAttribute().getName()
                ).stream().filter(
                        value -> !Objects.equals(
                                0,
                                value.trim().length()
                        )
                ).toArray(
                        String[]::new
                )
        );
    }

    public static DocumentCondition computeCondition(Predicate predicate) {
        DocumentCondition result = null;
        if (predicate instanceof CompositionPredicate) {
            Collection<Predicate> restrictions = CompositionPredicate.class.cast(predicate).getPredicates();
            Function<DocumentCondition[], DocumentCondition> function = predicate instanceof DisjunctionPredicate
                    ? DocumentCondition::or
                    : DocumentCondition::and;
            result = function.apply(
                    restrictions.stream().map(
                            restriction -> computeCondition(restriction)
                    ).collect(
                            Collectors.toList()
                    ).toArray(
                            DocumentCondition[]::new
                    )
            );
        } else if (predicate instanceof NegationPredicate) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else if (predicate instanceof BinaryPredicate) {
            BinaryPredicate cast = BinaryPredicate.class.cast(predicate);
            String lhs = unfold(
                    cast.getLeft()
            );
            Object rhs = cast.getRight();
            if (rhs instanceof Expression) {
                throw new UnsupportedOperationException("Not supported yet.");
            } else {
                BiFunction<String, Object, DocumentCondition> bifunction;
                switch (cast.getOperator()) {
                    case EQUAL:
                        bifunction = DocumentCondition::eq;
                        break;
                    case GREATER_THAN:
                        bifunction = DocumentCondition::gt;
                        break;
                    case GREATER_THAN_OR_EQUAL:
                        bifunction = DocumentCondition::gte;
                        break;
                    case LESS_THAN:
                        bifunction = DocumentCondition::lt;
                        break;
                    case LESS_THAN_OR_EQUAL:
                        bifunction = DocumentCondition::lte;
                        break;
                    case IN:
                        bifunction = DocumentCondition::in;
                        break;
                    case LIKE:
                        bifunction = DocumentCondition::like;
                        break;
                    default:
                        throw new UnsupportedOperationException("Not supported yet.");
                }
                result = bifunction.apply(lhs, rhs);
            }
        } else if (predicate instanceof RangePredicate) {
            RangePredicate cast = RangePredicate.class.cast(predicate);
            String lhs = unfold(
                    cast.getLeft()
            );
            Object from = cast.getFrom();
            Object to = cast.getTo();
            if (from instanceof Expression || to instanceof Expression) {
                throw new UnsupportedOperationException("Not supported yet.");
            } else {
                BiFunction<String, Object, DocumentCondition> bifunction;
                switch (cast.getOperator()) {
                    case EXCLUSIVE_BETWEEN:
                    case INCLUSIVE_BETWEEN:
                        bifunction = DocumentCondition::between;
                        break;
                    default:
                        throw new UnsupportedOperationException("Not supported yet.");
                }
                result = bifunction.apply(lhs, Arrays.asList(from, to));
            }
        }
        if (Objects.isNull(result)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return result;
    }

    public static <X> DocumentQuery convert(SelectQuery<X> selectQuery) {

        DocumentQuery.DocumentQueryBuilder builder;

        if (selectQuery instanceof ExpressionQuery) {
            ExpressionQuery<X> expressionQuery = ExpressionQuery.class.cast(selectQuery);
            builder = DocumentQuery.builder(
                    expressionQuery.getExpressions().stream().map(
                            expression -> unfold(expression)
                    ).toArray(String[]::new)
            );
        } else {
            builder = DocumentQuery.builder();
        }

        return builder.where(
                DocumentCondition.and(
                        Optional.ofNullable(
                                selectQuery.getRestrictions()
                        ).orElse(
                                Collections.<Predicate<X>>emptyList()
                        ).stream().map(
                                restriction -> computeCondition(
                                        restriction
                                )
                        ).toArray(
                                DocumentCondition[]::new
                        )
                )
        ).sort(
                Optional.ofNullable(
                        selectQuery.getOrderBy()
                ).orElse(
                        Collections.<Order<X, ?>>emptyList()
                ).stream().map(
                        orderBy -> {
                            String unfold = unfold(orderBy.getExpression());
                            return orderBy.isAscending() ? Sort.asc(unfold) : Sort.desc(unfold);
                        }
                ).toArray(
                        Sort[]::new
                )
        ).limit(
                selectQuery.getMaxResults()
        ).skip(
                selectQuery.getFirstResult()
        ).build();
    }

}
