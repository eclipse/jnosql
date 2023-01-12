package org.eclipse.jnosql.communication.query;


import org.eclipse.jnosql.communication.Condition;

/**
 * Condition performs different computations or actions depending on whether a boolean query
 * condition evaluates to true or false.
 * The conditions are composed of three elements.
 * The condition's name
 * The Operator
 * The Value
 *
 * @see QueryCondition#name() ()
 * @see QueryCondition#condition() ()
 * @see QueryCondition#value()
 */
public interface QueryCondition {

    /**
     * the data source or target, to apply the operator
     *
     * @return the name
     */
    String name();

    /**
     * that defines comparing process between the name and the value.
     *
     * @return the operator
     */
    Condition condition();

    /**
     * that data that receives the operation.
     *
     * @return the value
     */
    QueryValue<?> value();
}