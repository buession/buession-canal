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
import com.buession.canal.core.Table;
import com.buession.canal.core.event.invoker.EventMethodInvoker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件监听器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalEventListener implements Serializable {

	private final static long serialVersionUID = -8417968908960591491L;

	private final Map<Method, EventMethodInvoker> methodCache = new ConcurrentHashMap<>();

	/**
	 * 指令
	 */
	private final String destination;

	/**
	 * 数据库信息
	 */
	private final Table table;

	/**
	 * 事件类型
	 */
	private final CanalEntry.EventType eventType;

	/**
	 * {@link com.buession.canal.annotation.CanalBinding} 实例
	 */
	private final Object object;

	/**
	 * 方法
	 */
	private final Method method;

	/**
	 * 方法参数列表
	 */
	private final ParameterMapping[] parameterMappings;

	/**
	 * 方法调用者
	 */
	private final Invoker invoker;

	/**
	 * 构造函数
	 *
	 * @param destination
	 * 		指令
	 * @param table
	 * 		数据库信息
	 * @param eventType
	 * 		事件类型
	 * @param object
	 *        {@link com.buession.canal.annotation.CanalBinding} 实例
	 * @param method
	 * 		方法
	 * @param parameterMappings
	 * 		方法参数列表
	 */
	public CanalEventListener(final String destination, final Table table, final CanalEntry.EventType eventType,
							  final Object object, final Method method,
							  final ParameterMapping[] parameterMappings) {
		this.destination = destination;
		this.table = table;
		this.eventType = eventType;
		this.object = object;
		this.method = method;
		this.parameterMappings = parameterMappings;
		this.invoker = new Invoker(object.getClass(), eventType, methodCache);
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
	 * 返回数据库信息
	 *
	 * @return 数据库信息
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * 返回事件类型
	 *
	 * @return 事件类型
	 */
	public CanalEntry.EventType getEventType() {
		return eventType;
	}

	/**
	 * 返回 {@link com.buession.canal.annotation.CanalBinding} 实例
	 *
	 * @return {@link com.buession.canal.annotation.CanalBinding} 实例
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * 返回方法
	 *
	 * @return 方法
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 返回方法参数列表
	 *
	 * @return 方法参数列表
	 */
	public ParameterMapping[] getParameterMappings() {
		return parameterMappings;
	}

	/**
	 * 返回方法调用者
	 *
	 * @return 方法调用者
	 */
	public Invoker getInvoker() {
		return invoker;
	}

}
