package org.apache.diana.api.writer;

import org.apache.diana.api.ReaderField;
import org.apache.diana.api.WriterField;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by otaviojava on 22/07/16.
 */
public final class WriterFieldProvider {

    private static WriterFieldProvider INSTANCE = new WriterFieldProvider();

    private final List<WriterField> readers = new ArrayList<>();

    {
        ServiceLoader.load(WriterField.class).forEach(readers::add);
    }

    public static WriterFieldProvider getInstance() {
        return INSTANCE;
    }

    private WriterFieldProvider() {
    }
}
