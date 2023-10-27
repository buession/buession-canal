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
package com.buession.canal.spring;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.core.Callable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class Invoke implements Serializable {

	private final static long serialVersionUID = -7317688813549955905L;

	private Object target;

	private Method method;

	private Callable callable;

	private CanalEntry.EventType eventType;

	public Invoke() {
	}

	public Invoke(Object target, Method method, Callable callable, CanalEntry.EventType eventType) {
		this.target = target;
		this.method = method;
		this.callable = callable;
		this.eventType = eventType;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Callable getCallable() {
		return callable;
	}

	public void setCallable(Callable callable) {
		this.callable = callable;
	}

	public CanalEntry.EventType getEventType() {
		return eventType;
	}

	public void setEventType(CanalEntry.EventType eventType) {
		this.eventType = eventType;
	}

}
