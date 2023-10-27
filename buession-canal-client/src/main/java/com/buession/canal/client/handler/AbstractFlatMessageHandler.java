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
package com.buession.canal.core.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.buession.canal.core.Callable;
import com.buession.core.validator.Validate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Flat 消息处理器抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractFlatMessageHandler extends AbstractMessageHandler<FlatMessage>
		implements FlatMessageHandler {

	@Override
	public void handle(final FlatMessage message, final Callable callable) throws Exception {
		List<Map<String, String>> messageData = message.getData();

		if(Validate.isEmpty(messageData)){
			return;
		}

		String schemaName = message.getDatabase();
		String tableName = message.getTable();

		for(int i = 0, j = messageData.size(); i < j; i++){
			CanalEntry.EventType eventType = CanalEntry.EventType.valueOf(message.getType());

			List<Map<String, String>> maps;
			if(eventType.equals(CanalEntry.EventType.UPDATE)){
				Map<String, String> map = messageData.get(i);
				Map<String, String> oldMap = message.getOld().get(i);
				maps = Stream.of(map, oldMap).collect(Collectors.toList());
			}else{
				maps = Stream.of(messageData.get(i)).collect(Collectors.toList());
			}
		}

		callable.call(null, null, schemaName, tableName);
	}

}
