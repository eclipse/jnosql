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

import org.eclipse.jnosql.query.grammar.QueryParser;

import java.time.Duration;

final class Durations {

    private Durations() {
    }

    static Duration get(QueryParser.TtlContext ctx) {
        long value = Long.parseLong(ctx.INT().getText());
        String unit = ctx.unit().getText();
        return switch (unit) {
            case "day" -> Duration.ofDays(value);
            case "hour" -> Duration.ofHours(value);
            case "minute" -> Duration.ofMinutes(value);
            case "second" -> Duration.ofSeconds(value);
            case "millisecond" -> Duration.ofMillis(value);
            case "nanosecond" -> Duration.ofNanos(value);
            default -> throw new UnsupportedOperationException("There isn't support for this unit to TTL: " + unit);
        };
    }

}
