package org.apache.diana.redis.key;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

public class RedisMap<T> implements Map<String, T> {


	private Class<T> clazz;

	private String nameSpace;

	private Jedis jedis;

	private Gson gson;


	RedisMap(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
		this.clazz = clazz;
		this.nameSpace = keyWithNameSpace;
		this.jedis = jedis;
		gson = new Gson();
	}

	@Override
	public int size() {
		return jedis.hgetAll(nameSpace).size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return jedis.hexists(nameSpace, Objects.requireNonNull(key).toString());
	}

	@Override
	public boolean containsValue(Object value) {
		Objects.requireNonNull(value);
		String valueString = gson.toJson(value);
		Map<String, String> map = createRedisMap();
		return map.containsValue(valueString);
	}

	@Override
	public T get(Object key) {
		Objects.requireNonNull(key);
		String value = jedis.hget(nameSpace, key.toString());
		if (StringUtils.isNoneBlank(value)) {
			return gson.fromJson(value, clazz);
		}
		return null;
	}

	@Override
	public T put(String key, T value) {
		Objects.requireNonNull(value);
		jedis.hset(nameSpace, Objects.requireNonNull(key), gson.toJson(value));
		return value;
	}

	@Override
	public T remove(Object key) {
		T value = get(key);
		if (value != null){
			jedis.hdel(nameSpace, Objects.requireNonNull(key).toString());
			return value;
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> map) {
		Objects.requireNonNull(map);

		for(String key: map.keySet()) {
			T value = map.get(key);
			if (value != null) {
				put(key, value);
			}
		}
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Remove all elements using remove key in MapStructure");

	}

	@Override
	public Set<String> keySet() {
		return createRedisMap().keySet();
	}

	@Override
	public Collection<T> values() {
		return createHashMap().values();
	}

	@Override
	public Set<Entry<String, T>> entrySet() {
		return createHashMap().entrySet();
	}

	private Map<String, String> createRedisMap() {
		Map<String, String> map = jedis.hgetAll(nameSpace);
		return map;
	}

	private Map<String, T> createHashMap() {
		Map<String, T> values = new HashMap<>();
		Map<String, String> redisMap = createRedisMap();

		for(String key: redisMap.keySet()) {
			values.put(key, gson.fromJson(redisMap.get(key), clazz));
		}
		return values;
	}

	@Override
	public String toString() {
		return "Aooonde?: br.com.elo7.elodum.redis.builder.RedisMap at " + nameSpace;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nameSpace);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (RedisMap.class.isInstance(obj)) {
			RedisMap otherRedis = RedisMap.class.cast(obj);
			return Objects.equals(otherRedis.nameSpace, nameSpace);
		}
		return false;
	}

}
