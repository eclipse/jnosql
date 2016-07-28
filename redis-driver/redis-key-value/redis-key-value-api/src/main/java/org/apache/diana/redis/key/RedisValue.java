package org.apache.diana.redis.key;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.diana.api.Value;

import java.lang.reflect.Type;
import java.util.*;

final class RedisValue implements Value {

    private final Gson gson;

    private final String json;

    public static Value of(Gson gson, String json) {
        return new RedisValue(gson, json);
    }

    private RedisValue(Gson gson, String json) {
        this.gson = gson;
        this.json = json;
    }

    @Override
    public Object get() {
        return json;
    }

    @Override
    public <T> T cast() throws ClassCastException {
        return (T) json;
    }

    @Override
    public <T> T get(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> List<T> getList(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {

        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public <T> Set<T> getSet(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {
        Type type = new TypeToken<HashSet<T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) throws NullPointerException, UnsupportedOperationException {
        Type type = new TypeToken<Map<K, V>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisValue that = (RedisValue) o;
        return Objects.equals(json, that.json);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(json);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedisValue{");
        sb.append("gson=").append(gson);
        sb.append(", json='").append(json).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
