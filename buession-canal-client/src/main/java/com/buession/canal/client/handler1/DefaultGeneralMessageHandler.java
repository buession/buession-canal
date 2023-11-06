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
import com.alibaba.otter.canal.protocol.Message;

import java.util.List;

/**
 * 默认常规消息处理器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class DefaultGeneralMessageHandler extends AbstractGeneralMessageHandler {

	@Override
	public void handle(final Message message) throws Exception {
		List<CanalEntry.Entry> entries = message.getEntries();
		List<CanalEntry.EntryType> ignoreEntryTypes = getIgnoreEntryTypes();

		for(CanalEntry.Entry entry : entries){
			if(ignoreEntryTypes.stream().anyMatch(t->entry.getEntryType() == t)){
				continue;
			}

			doHandle(entry);
		}
	}

	private void doHandle(final CanalEntry.Entry entry) {
		String schemaName = entry.getHeader().getSchemaName();
		String tableName = entry.getHeader().getTableName();

		CanalEntry.RowChange rowChange;
		try{
			rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
		}catch(Exception e){
			throw new RuntimeException("parse event has an error, data: " + entry, e);
		}
		
		CanalEntry.EventType eventType = rowChange.getEventType();

		try{
			for(CanalEntry.RowData rowData : rowChange.getRowDatasList()){
				System.out.println(rowData);
			}
		}catch(Exception e){
			logger.error("handle row data error", e);
		}
	}

}
