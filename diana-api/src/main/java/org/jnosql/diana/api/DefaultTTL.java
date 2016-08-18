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
package org.jnosql.diana.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The default implementation to {@link TTL}
 */
class DefaultTTL implements TTL {

    private final long duration;

    private final TimeUnit timeUnit;

    DefaultTTL(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }


    @Override
    public long toNanos() {
        return timeUnit.toNanos(duration);
    }

    @Override
    public long toMicros() {
        return timeUnit.toMicros(duration);
    }

    @Override
    public long toMillis() {
        return timeUnit.toMillis(duration);
    }

    @Override
    public long toSeconds() {
        return timeUnit.toSeconds(duration);
    }

    @Override
    public long toMinutes() {
        return timeUnit.toMinutes(duration);
    }

    @Override
    public long toDays() {
        return timeUnit.toDays(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TTL)) {
            return false;
        }
        TTL that = (TTL) o;
        return this.toMillis() == that.toMillis();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toMillis());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("duration", duration)
                .append("timeUnit", timeUnit)
                .toString();
    }
}
