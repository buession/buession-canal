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

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.buession.canal.client.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Canal Kafka 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class KafkaCanalAdapterClient extends AbstractCanalAdapterClient<KafkaCanalConnector> {

	private final static Logger logger = LoggerFactory.getLogger(KafkaCanalAdapterClient.class);

	/**
	 * 构造函数
	 *
	 * @param servers
	 * 		Kafka 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param partition
	 * 		partition
	 * @param messageHandler
	 * 		消息处理器
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final MessageHandler<?> messageHandler) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, 1), messageHandler);
	}

	/**
	 * 构造函数
	 *
	 * @param servers
	 * 		Kafka 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param partition
	 * 		partition
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final MessageHandler<?> messageHandler,
								   final Long timeout) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, 1), messageHandler, timeout);
	}

	/**
	 * 构造函数
	 *
	 * @param servers
	 * 		Kafka 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param partition
	 * 		partition
	 * @param messageHandler
	 * 		消息处理器
	 * @param batchSize
	 * 		批处理条数
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final MessageHandler<?> messageHandler,
								   final Integer batchSize) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, batchSize), messageHandler, batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param servers
	 * 		Kafka 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param partition
	 * 		partition
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 * @param batchSize
	 * 		批处理条数
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final MessageHandler<?> messageHandler, final Long timeout,
								   final Integer batchSize) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, batchSize), messageHandler, timeout,
				batchSize);
	}

	@Override
	protected void process() {
		try{
			List<FlatMessage> messages = getConnector().getFlatListWithoutAck(getTimeout(), TIMEOUT_UNIT);

			if(logger.isDebugEnabled()){
				logger.debug("Receive messages = {}", messages);
			}

			for(FlatMessage message : messages){
				getMessageHandler().handle(message);
			}

			getConnector().ack(); // 提交确认
		}catch(Exception e){
			logger.error("Handle message error, rollback", e);
			getConnector().rollback();
		}
	}

	protected static KafkaCanalConnector createKafkaCanalConnector(final String servers, final String topic,
																   final String groupId, final Integer partition,
																   final Integer batchSize) {
		return new KafkaCanalConnector(servers, topic, partition, groupId, batchSize, true);
	}

}
