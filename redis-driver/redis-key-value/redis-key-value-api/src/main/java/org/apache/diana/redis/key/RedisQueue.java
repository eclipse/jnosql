package org.apache.diana.redis.key;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

class RedisQueue<T> extends RedisCollection<T> implements Queue<T> {


	public RedisQueue(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
		super(jedis, clazz, keyWithNameSpace);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Remove all elements using remove key in QueueStructure");
	}

	@Override
	public boolean add(T e) {
		Objects.requireNonNull(e);
		jedis.rpush(keyWithNameSpace, gson.toJson(e));
		return true;
	}

	@Override
	public boolean offer(T e) {
		return add(e);
	}

	@Override
	public T remove() {
		T value = poll();
		if (value == null) {
			throw new NoSuchElementException("No element in Redis Queue");
		}
		return value;
	}

	@Override
	public T poll() {
		String value = jedis.lpop(keyWithNameSpace);
		if (StringUtils.isNoneBlank(value)) {
			return gson.fromJson(value, clazz);
		}
		return null;
	}

	@Override
	public T element() {
		T value = peek();
		if (value == null) {
			throw new NoSuchElementException("No element in Redis Queue");
		}
		return value;
	}

	@Override
	public T peek() {
		int index = size();
		if (index ==0) {
			return null;
		}
		return gson.fromJson(jedis.lindex(keyWithNameSpace, (long)index-1), clazz);
	}
	@Override
	public String toString() {
		return "Aooonde?: br.com.elo7.elodum.redis.builder.RedisQueue at " + keyWithNameSpace;
	}
}
