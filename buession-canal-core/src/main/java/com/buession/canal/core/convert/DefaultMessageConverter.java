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
import com.buession.canal.core.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认信息转换器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class DefaultMessageConverter extends AbstractMessageConverter<Message> {

	private final List<CanalEntry.EntryType> ignoreEntryTypes = getIgnoreEntryTypes();

	@Override
	public List<CanalMessage> convert(final String destination, final Message message) {
		List<CanalEntry.Entry> entries = message.getEntries();

		return entries.stream()
				.filter((entry)->ignoreEntryTypes.stream().anyMatch(t->entry.getEntryType() == t) == false)
				.map((entry)->this.doParseEntry(destination, entry)).collect(Collectors.toList());
	}

	private CanalMessage doParseEntry(final String destination, final CanalEntry.Entry entry) {
		CanalEntry.RowChange rowChange;
		try{
			rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
		}catch(Exception e){
			throw new RuntimeException("parse event has an error, data: " + entry, e);
		}

		final List<CanalEntry.Column> data = new ArrayList<>(rowChange.getRowDatasList().size());

		rowChange.getRowDatasList().forEach((rowData)->data.addAll(rowData.getAfterColumnsList()));

		final CanalMessage canalMessage = new CanalMessage();

		canalMessage.setDestination(destination);
		canalMessage.setTable(new Table(entry.getHeader().getTableName(), entry.getHeader().getSchemaName()));
		canalMessage.setEntryType(entry.getEntryType());
		canalMessage.setEventType(entry.getHeader().getEventType());
		canalMessage.setHeader(entry.getHeader());
		canalMessage.setRowChange(rowChange);
		canalMessage.setData(data);
		canalMessage.setDdl(rowChange.getIsDdl());

		return canalMessage;
	}

	@Override
	protected List<CanalEntry.EntryType> getIgnoreEntryTypes() {
		return Arrays.asList(CanalEntry.EntryType.TRANSACTIONBEGIN, CanalEntry.EntryType.TRANSACTIONEND,
				CanalEntry.EntryType.HEARTBEAT);
	}

}
