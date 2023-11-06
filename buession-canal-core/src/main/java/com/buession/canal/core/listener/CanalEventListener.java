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
package com.buession.canal.core.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalEventListener {

	/**
	 * 指令
	 */
	private final String destination;

	/**
	 * 事件类型
	 */
	private final CanalEntry.EventType eventType;

	private final Object object;

	private final Method method;

	private List<ParameterMapping> parameterMappings;

	/**
	 * 构造函数
	 *
	 * @param destination
	 * 		指令
	 * @param eventType
	 * 		事件类型
	 * @param object
	 * 		-
	 * @param method
	 * 		-
	 * @param parameterMappings
	 * 		-
	 */
	public CanalEventListener(final String destination, final CanalEntry.EventType eventType, final Object object,
							  final Method method, final List<ParameterMapping> parameterMappings) {
		this.destination = destination;
		this.eventType = eventType;
		this.object = object;
		this.method = method;
		this.parameterMappings = parameterMappings;
	}

	/**
	 * 返回指令
	 *
	 * @return 指令
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * 返回事件类型
	 *
	 * @return 事件类型
	 */
	public CanalEntry.EventType getEventType() {
		return eventType;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

}
