package org.apache.diana.api.key;


import org.apache.diana.api.Value;

import java.io.Serializable;

public interface KeyValue extends Serializable {

    static KeyValue of(String key, Value value) {
        return new DefaultKeyValue(key, value);
    }

    String getKey();

    Value getValue();

}
