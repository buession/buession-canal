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
package com.buession.canal.core.convert;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.buession.canal.core.CanalMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认信息转换器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class DefaultMessageTransponder extends AbstractMessageTransponder<Message> {

	@Override
	public List<CanalMessage> convert(final Message message) {
		List<CanalEntry.Entry> entries = message.getEntries();
		List<CanalMessage> messages = new ArrayList<>(entries.size());

		for(CanalEntry.Entry entry : entries){
			CanalEntry.RowChange rowChange;

			try{
				// 获取信息改变
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

			}catch(Exception e){
				throw new RuntimeException("错误 ##转换错误 , 数据信息:" + entry.toString(), e);
			}

			messages.add(distribute(null, entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
					rowChange));
		}

		return messages;
	}

	protected CanalMessage distribute(final String destination, final String schemaName, final String tableName,
									  final CanalEntry.RowChange rowChange) {
		final CanalMessage canalMessage = new CanalMessage();

		canalMessage.setDestination(destination);
		canalMessage.setSchemaName(schemaName);
		canalMessage.setTableName(tableName);
		canalMessage.setRowChange(rowChange);

		return canalMessage;
	}

}
