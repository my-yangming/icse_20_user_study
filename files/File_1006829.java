/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package x7.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import x7.config.SpringHelper;

import java.util.List;
import java.util.Set;

public class JedisConnector_Cache {

	private RedisTemplate redisTemplate;

	private StringRedisTemplate stringRedisTemplate;
	
	private static JedisConnector_Cache instance;

	
	public static JedisConnector_Cache getInstance(){
		if (instance == null){
			instance = new JedisConnector_Cache();

		}
		return instance;
	}

	private void init(){
		redisTemplate = (RedisTemplate) SpringHelper.getObject("redisTemplate");
		stringRedisTemplate = (StringRedisTemplate)SpringHelper.getObject("stringRedisTemplate");
	}
	
	private JedisConnector_Cache(){
		init();
	}
	

	public boolean set(String key, String value){
		if (key == null || key.equals("") ) 
			return false;
		this.stringRedisTemplate.opsForValue().set(key, value);
		return true;
	}
	
	public boolean set(byte[] key, byte[] value){

		this.redisTemplate.opsForValue().set(key, value);

		return true;
	}

	public boolean set(String key, String value,int validSeconds){
		if (key == null || key.equals("") )
			return false;
		this.stringRedisTemplate.opsForValue().set(key, value,validSeconds);
		return true;
	}
	
	public boolean set(byte[] key, byte[] value, int validSeconds){

		this.redisTemplate.opsForValue().set(key, value,validSeconds);
		return true;
	}
	
	public String get(String key){

		String str = this.stringRedisTemplate.opsForValue().get(key);
		if (str == null)
			return str;
		return str.trim();
	}
	
//	public List<byte[]> mget(List<byte[]> keyList){
//
//		if (keyList == null || keyList.isEmpty())
//			return null;
//
//		Object obj = this.redisTemplate.opsForValue().multiGet(keyList);
//		if (obj == null)
//			return null;
//		return (List<byte[]>) obj;
//
//	}

	public List<String> mget(List<String> keyList){

		if (keyList == null || keyList.isEmpty())
			return null;

		List<String> list = this.stringRedisTemplate.opsForValue().multiGet(keyList);
		if (list == null)
			return null;
		return list;

	}
	
	public byte[] get(byte[] key){

		Object obj = this.redisTemplate.opsForValue().get(key);
		
		return (byte[])obj;
	}
	
	public boolean delete(byte[] key){

		this.redisTemplate.delete(key);
		return true;
	}

	public boolean delete(String key){

		this.stringRedisTemplate.delete(key);
		return true;
	}

	public Set<String> keys(String pattern){

		Set<String> set = this.stringRedisTemplate.keys(pattern);

		return set;
	}

}
