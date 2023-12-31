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
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认信息转换器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class FlatMessageConverter extends AbstractMessageConverter<FlatMessage> {

	@Override
	public List<CanalMessage> convert(final String destination, final FlatMessage message) {
		return message.getData().stream().map((row)->this.doParseEntry(destination, message, row))
				.collect(Collectors.toList());
	}

	private CanalMessage doParseEntry(final String destination, final FlatMessage message,
									  final Map<String, String> row) {
		final CanalMessage canalMessage = new CanalMessage();
		final List<CanalEntry.Column> columns = new ArrayList<>(row.size());
		int i = 0;

		for(Map.Entry<String, String> e : row.entrySet()){
			CanalEntry.Column.Builder builder = CanalEntry.Column.newBuilder();

			builder.setIndex(i++);
			builder.setName(e.getKey());
			builder.setValue(e.getValue());

			columns.add(builder.build());
		}

		canalMessage.setDestination(destination);
		canalMessage.setTable(new Table(message.getTable(), message.getDatabase()));
		canalMessage.setEventType(CanalEntry.EventType.valueOf(message.getType()));
		canalMessage.setData(columns);
		canalMessage.setDdl(message.getIsDdl());

		return canalMessage;
	}

}
