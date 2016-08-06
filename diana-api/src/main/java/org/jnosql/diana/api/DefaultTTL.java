package org.jnosql.diana.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The default implementation to {@link TTL}
 *
 * @author Otávio Santana
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
