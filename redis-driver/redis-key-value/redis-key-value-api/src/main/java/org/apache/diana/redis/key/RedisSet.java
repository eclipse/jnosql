package org.apache.diana.redis.key;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import redis.clients.jedis.Jedis;

class RedisSet<T> extends RedisCollection<T> implements Set<T>  {

	RedisSet(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
		super(jedis, clazz, keyWithNameSpace);
	}

	@Override
	public boolean add(T e) {
		Objects.requireNonNull(e);
		jedis.sadd(keyWithNameSpace, gson.toJson(e));
		return false;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Remove all elements using remove key in SetStructure");
	}

	@Override
	public int size() {
		return jedis.scard(keyWithNameSpace).intValue();
	}

	@Override
	protected int indexOf(Object o) {
		Objects.requireNonNull(o);
		String find = gson.toJson(o);
		Set<String> values = jedis.smembers(keyWithNameSpace);
		int index = 0;
		for(String value: values) {
			if(value.contains(find)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	@Override
	protected T remove(int index) {
		T element = toArrayList().get(index);
		if (element == null) {
			return null;
		}
		remove(element);
		return element;
	}

	@Override
	public boolean remove(Object o) {
		if (!clazz.isInstance(o)) {
			throw new ClassCastException("The object required is " + clazz.getName());
		}
		String find = gson.toJson(o);
		Set<String> values = jedis.smembers(keyWithNameSpace);
		for(String value: values) {
			if(value.contains(find)) {
				jedis.srem(keyWithNameSpace, value);
				return true;
			}
		}
		return false;
	}

	@Override
	protected List<T> toArrayList() {
		Set<String> redisValues = jedis.smembers(keyWithNameSpace);
		List<T> list = new ArrayList<>();
		for(String redisValue: redisValues) {
			list.add(gson.fromJson(redisValue, clazz));
		}
		return list;
	}

	@Override
	public String toString() {
		return "Aooonde?: br.com.elo7.elodum.redis.builder.RedisSet at " + keyWithNameSpace;
	}

}
