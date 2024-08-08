package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.json.bind.config.PropertyVisibilityStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldAccessStrategy implements PropertyVisibilityStrategy {
    @Override
    public boolean isVisible(Field field) {
        return true;
    }

    @Override
    public boolean isVisible(Method method) {
        return true;
    }
}
