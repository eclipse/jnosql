package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link WriterField} supported by Diana
 *
 * @author Otávio Santana
 * @see WriterField
 */
public final class WriterFieldDecorator implements WriterField {

    private static WriterFieldDecorator INSTANCE = new WriterFieldDecorator();

    private final List<WriterField> writers = new ArrayList<>();

    {
        ServiceLoader.load(WriterField.class).forEach(writers::add);
    }

    public static WriterFieldDecorator getInstance() {
        return INSTANCE;
    }

    private WriterFieldDecorator() {
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return writers.stream().anyMatch(writerField -> writerField.isCompatible(clazz));
    }

    @Override
    public Object write(Object object) {
        Class clazz = object.getClass();
        WriterField writerField = writers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return writerField.write(object);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WriterFieldDecorator{");
        sb.append("writers=").append(writers);
        sb.append('}');
        return sb.toString();
    }
}
