/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * =========================================================================================================
 *
 * This software consists of voluntary contributions made by many individuals on behalf of the
 * Apache Software Foundation. For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * +-------------------------------------------------------------------------------------------------------+
 * | License: http://www.apache.org/licenses/LICENSE-2.0.txt 										       |
 * | Author: Yong.Teng <webmaster@buession.com> 													       |
 * | Copyright @ 2013-2023 Buession.com Inc.														       |
 * +-------------------------------------------------------------------------------------------------------+
 */
package com.buession.canal.springboot.autoconfigure;

import com.buession.canal.springboot.BaseInstanceConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Canal 适配器配置基类
 *
 * @author Yong.Teng
 * @since 1.0.0
 */
abstract class AbstractAdapterProperties<IC extends BaseInstanceConfiguration>
		implements AdapterProperties<IC> {

	/**
	 * 实例清单
	 */
	private Map<String, IC> instances = new HashMap<>();

	/**
	 * 返回实例清单
	 *
	 * @return 实例清单
	 */
	@Override
	public Map<String, IC> getInstances() {
		return instances;
	}

	/**
	 * 设置实例清单
	 *
	 * @param instances
	 * 		实例清单
	 */
	public void setInstances(Map<String, IC> instances) {
		this.instances = instances;
	}

}
