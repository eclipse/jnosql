package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;

public final class WriterFields {

    private WriterFields() {}

    public static WriterField get() {
        return WriterFieldDecorator.getInstance();
    }

}
