package org.apache.diana.api.util;


import com.sun.istack.internal.Nullable;

import java.io.Serializable;

final class NumberUtils {

    private NumberUtils() {}

    public boolean isNumberInstance(Serializable model) {
        return Number.class.isInstance(model);
    }
}
