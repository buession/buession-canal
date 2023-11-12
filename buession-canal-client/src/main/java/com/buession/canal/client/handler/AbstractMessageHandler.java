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

import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.EventHandler;
import com.buession.canal.core.listener.MethodParameter;

import java.util.function.Function;

/**
 * 信息处理抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractMessageHandler implements MessageHandler {

	public AbstractMessageHandler() {
	}

	@Override
	public Object handle(final EventHandler eventHandler) {
		return null;
	}

	protected Function<MethodParameter, Object> convertParameter(final CanalMessage message) {
		return (pm)->{
			/*

			ParameterizedType parameterizedType = ((ParameterizedType) pm.getType());
			Type type = parameterizedType.getRawType();
			Class<?> clazz = (Class<?>) type;

			if(Collection.class.isAssignableFrom(clazz)){
				if(Objects.equals(parameterizedType.getActualTypeArguments()[0], CanalEntry.RowData.class)){
					return message.getRowChange().getRowDatasList();
				}
			}

			 */

			return null;
		};
	}

}
