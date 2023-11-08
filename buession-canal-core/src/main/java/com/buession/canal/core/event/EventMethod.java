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
package com.buession.canal.core.event;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class EventMethod {

	private final Method method;

	private final CanalEntry.EventType eventType;

	public EventMethod(Class<?> bindingType, final Method method, final CanalEntry.EventType eventType) {
		this.method = method;
		this.eventType = eventType;
	}

	public Object execute(Object object, Object[] args) throws InvocationTargetException, IllegalAccessException {
		switch(eventType){
			case INSERT:
				return method.invoke(object, args);
			case UPDATE:
				return method.invoke(object, args);
			case DELETE:
				return method.invoke(object, args);
			case CREATE:
				return method.invoke(object, args);
			case ALTER:
				return method.invoke(object, args);
			case ERASE:
				return method.invoke(object, args);
			case QUERY:
				return method.invoke(object, args);
			case TRUNCATE:
				return method.invoke(object, args);
			case RENAME:
				return method.invoke(object, args);
			case CINDEX:
				return method.invoke(object, args);
			case DINDEX:
				return method.invoke(object, args);
			case GTID:
				return method.invoke(object, args);
			case XACOMMIT:
				return method.invoke(object, args);
			case XAROLLBACK:
				return method.invoke(object, args);
			case MHEARTBEAT:
				return method.invoke(object, args);
			default:
				break;
		}

		return null;
	}

}
