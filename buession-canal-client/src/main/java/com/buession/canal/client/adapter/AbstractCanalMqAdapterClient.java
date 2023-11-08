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
package com.buession.canal.client.adapter;

import com.alibaba.otter.canal.client.CanalMQConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.canal.core.CanalMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Canal MQ 适配器抽象类
 *
 * @param <C>
 * 		Canal 数据操作客户端
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalMqAdapterClient<C extends CanalMQConnector> extends AbstractCanalAdapterClient<C>
		implements CanalMqAdapterClient {

	private final boolean flatMessage;

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 */
	public AbstractCanalMqAdapterClient(final C connector, final String destination) {
		this(connector, destination, DEFAULT_FLAT_MESSAGE);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalMqAdapterClient(final C connector, final String destination, final int batchSize) {
		this(connector, destination, batchSize, DEFAULT_FLAT_MESSAGE);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 * @param flatMessage
	 * 		true / false
	 */
	public AbstractCanalMqAdapterClient(final C connector, final String destination, final boolean flatMessage) {
		super(connector, destination);
		this.flatMessage = flatMessage;
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 * @param batchSize
	 * 		批处理条数
	 * @param flatMessage
	 * 		true / false
	 */
	public AbstractCanalMqAdapterClient(final C connector, final String destination, final int batchSize,
										final boolean flatMessage) {
		super(connector, destination, batchSize);
		this.flatMessage = flatMessage;
	}

	@Override
	public boolean isFlatMessage() {
		return flatMessage;
	}

	@Override
	public List<CanalMessage> getList(Long timeout, TimeUnit unit) throws CanalClientException {
		if(isFlatMessage()){
			List<FlatMessage> messages = getConnector().getFlatList(timeout, unit);
			return flatMessagesConvert(messages);
		}else{
			List<Message> messages = getConnector().getList(timeout, unit);
			return messagesConvert(messages);
		}
	}

	@Override
	public List<CanalMessage> getListWithoutAck(Long timeout, TimeUnit unit) throws CanalClientException {
		if(isFlatMessage()){
			List<FlatMessage> messages = getConnector().getFlatListWithoutAck(timeout, unit);
			return flatMessagesConvert(messages);
		}else{
			List<Message> messages = getConnector().getListWithoutAck(timeout, unit);
			return messagesConvert(messages);
		}
	}

	@Override
	public void ack() throws CanalClientException {
		getConnector().ack();
	}

	@SuppressWarnings({"unchecked"})
	protected List<CanalMessage> flatMessagesConvert(final List<FlatMessage> messages) {
		List<CanalMessage> result = new ArrayList<>(messages.size());

		for(FlatMessage flatMessage : messages){
			//result.addAll(getMessageTransponder().convert(flatMessage));
		}

		return result;
	}

}
