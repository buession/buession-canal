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
package com.buession.canal.core.binding;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.annotation.CanalEventListener;
import com.buession.canal.core.exception.BindingException;
import com.buession.canal.core.session.CanalSession;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class EventMethod {

	private final Event event;

	private final MethodSignature method;

	public EventMethod(Class<?> listenerInterface, Method method) {
		this.event = new Event(listenerInterface, method);
		this.method = new MethodSignature(method);
	}

	public Object execute(CanalSession session, Object[] args) {
		switch(this.event.getEventType()){
			case INSERT:
				//event.onInsert();
				break;
			case UPDATE:
				//event.onUpdate();
				break;
			case DELETE:
				//event.onDelete();
				break;
			case CREATE:
				//event.onCreate();
				break;
			case ALTER:
				//event.onAlert();
				break;
			case ERASE:
				//event.onErase();
				break;
			case QUERY:
				//event.onQuery();
				break;
			case TRUNCATE:
				//event.onTruncate();
				break;
			case RENAME:
				//event.onRename();
				break;
			case CINDEX:
				//event.onCIndex();
				break;
			case DINDEX:
				//event.onDIndex();
				break;
			case GTID:
				//event.onGtid();
				break;
			case XACOMMIT:
				//event.onXAcommit();
				break;
			case XAROLLBACK:
				//event.onXArollback();
				break;
			case MHEARTBEAT:
				//event.onMHeartbeat();
				break;
			default:
				break;
		}

		return null;
	}

	public final static class Event {

		private final String name;

		private final CanalEntry.EventType eventType;

		public Event(Class<?> listenerInterface, Method method) {
			final String methodName = method.getName();
			this.name = listenerInterface.getCanonicalName() + '@' + methodName;

			CanalEventListener canalEventListener = AnnotationUtils.findAnnotation(method, CanalEventListener.class);
			eventType = canalEventListener == null ? null : canalEventListener.eventType();
			if(eventType == null){
				throw new BindingException("Unknown execution method event type for: " + name);
			}
		}

		public String getName() {
			return name;
		}

		public CanalEntry.EventType getEventType() {
			return eventType;
		}

	}

	public static class MethodSignature {

		public MethodSignature(Method method) {

		}

	}

}
