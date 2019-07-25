/*
 * Copyright 2018 Qunar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qunar.tc.qmq.task.database;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


/**
 * Created by IntelliJ IDEA.
 * User: liuzz
 * Date: 13-1-8
 * Time: 上�?�11:52
 */
public class DatabaseDriverMapping {

	private final Map<String, IDatabaseDriver> map;

	public DatabaseDriverMapping() {
		this.map = new HashMap<>();
	}

	public void init() {
		ServiceLoader<IDatabaseDriver> drivers = ServiceLoader.load(IDatabaseDriver.class);
		for (IDatabaseDriver driver : drivers) {
			map.put(driver.protocol(), driver);
		}
	}

	public IDatabaseDriver getDatabaseMapping(String protocol) {
		return map.get(protocol);
	}
}
