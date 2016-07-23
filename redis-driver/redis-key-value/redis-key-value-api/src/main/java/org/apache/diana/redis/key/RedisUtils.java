package org.apache.diana.redis.key;

import org.apache.commons.lang3.StringUtils;

abstract class RedisUtils {


	public static String createKeyWithNameSpace(String key, String nameSpace) {
		if (StringUtils.isBlank(key)) {
			throw new IrregularKeyValue("Key in KeyvalueStructure cannont be empty");
		}
		return nameSpace + ":" + key;
	}

}
