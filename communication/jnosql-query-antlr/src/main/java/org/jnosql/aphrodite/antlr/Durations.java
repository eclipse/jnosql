/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr;

import java.time.Duration;

final class Durations {

    private Durations() {
    }

    static Duration get(QueryParser.TtlContext ctx) {
        long value = Long.valueOf(ctx.INT().getText());
        String unit = ctx.unit().getText();
        switch (unit) {
            case "day":
                return Duration.ofDays(value);
            case "hour":
                return Duration.ofHours(value);
            case "minute":
                return Duration.ofMinutes(value);
            case "second":
                return Duration.ofSeconds(value);
            case "millisecond":
                return Duration.ofMillis(value);
            case "nanosecond":
                return Duration.ofNanos(value);
                default:
                    throw new UnsupportedOperationException("There isn't support for this unit to TTL: " + unit);

        }
    }

}
