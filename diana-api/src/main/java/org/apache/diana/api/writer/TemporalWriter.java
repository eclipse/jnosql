package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;

import java.time.temporal.Temporal;


public class TemporalWriter implements WriterField<Temporal, String> {

    @Override
    public boolean isCompatible(Class clazz) {
        return Temporal.class.isAssignableFrom(clazz);
    }

    @Override
    public String write(Temporal object) {
        return object.toString();
    }
}
