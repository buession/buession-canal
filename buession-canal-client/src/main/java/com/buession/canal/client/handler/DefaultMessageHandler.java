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
package com.buession.canal.client.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.client.Binder;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.CanalEventListener;
import com.buession.core.validator.Validate;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 默认信息处理
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class DefaultMessageHandler extends AbstractMessageHandler {

	public DefaultMessageHandler(final Binder binder) {
		super(binder);
	}

	@Override
	protected void distributeEvent(final CanalMessage message) {
		if(getBinding().getListeners() == null){
			return;
		}

		getBinding().getListeners().stream()
				.filter(eventListenerFilter(message.getTable().getSchema(), message.getTable().getName(),
						message.getEventType()))
				.forEach((listener)->{
					try{
						listener.getInvoker()
								.invoke(listener.getObject(), listener.getMethod(), getArgs(listener, message));
					}catch(Throwable e){
						logger.error("Invoker invoke error", e);
					}
				});
	}

	protected Predicate<CanalEventListener> eventListenerFilter(final String schemaName, final String tableName,
																final CanalEntry.EventType eventType) {
		// 判断数据库名是否一致
		Predicate<CanalEventListener> sf =
				listener->schemaName == null || Validate.isBlank(listener.getTable().getSchema())
						|| Objects.equals(schemaName, listener.getTable().getSchema());

		// 判断数据表名是否一致
		Predicate<CanalEventListener> tf =
				listener->tableName == null || Validate.isBlank(listener.getTable().getName())
						|| Objects.equals(tableName, listener.getTable().getName());

		// 判断事件类型是否一致
		Predicate<CanalEventListener> ef =
				listener->eventType == null || listener.getEventType() == null
						|| eventType == listener.getEventType();

		return sf.and(tf).and(ef);
	}

}
