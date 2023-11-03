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

import com.alibaba.otter.canal.client.rabbitmq.RabbitMQCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.buession.canal.client.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Canal RabbitMQ 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class RabbitMqCanalAdapterClient extends AbstractCanalAdapterClient<RabbitMQCanalConnector> {

	private final static Logger logger = LoggerFactory.getLogger(RabbitMqCanalAdapterClient.class);

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		RabbitMQ 主机地址
	 * @param virtualHost
	 * 		Virtual Host
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param queueName
	 * 		队列名称
	 * @param messageHandler
	 * 		消息处理器
	 */
	public RabbitMqCanalAdapterClient(final String server, final String virtualHost, final String username,
									  final String password, final String queueName,
									  final MessageHandler<?> messageHandler) {
		super(createRabbitMQCanalConnector(server, virtualHost, queueName, username, password), messageHandler);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		RabbitMQ 主机地址
	 * @param virtualHost
	 * 		Virtual Host
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param queueName
	 * 		队列名称
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 */
	public RabbitMqCanalAdapterClient(final String server, final String virtualHost, final String username,
									  final String password, final String queueName,
									  final MessageHandler<?> messageHandler, final Long timeout) {
		super(createRabbitMQCanalConnector(server, virtualHost, queueName, username, password), messageHandler,
				timeout);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		RabbitMQ 主机地址
	 * @param virtualHost
	 * 		Virtual Host
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param queueName
	 * 		队列名称
	 * @param messageHandler
	 * 		消息处理器
	 * @param batchSize
	 * 		批处理条数
	 */
	public RabbitMqCanalAdapterClient(final String server, final String virtualHost, final String username,
									  final String password, final String queueName,
									  final MessageHandler<?> messageHandler, final Integer batchSize) {
		super(createRabbitMQCanalConnector(server, virtualHost, queueName, username, password), messageHandler,
				batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		RabbitMQ 主机地址
	 * @param virtualHost
	 * 		Virtual Host
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param queueName
	 * 		队列名称
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 * @param batchSize
	 * 		批处理条数
	 */
	public RabbitMqCanalAdapterClient(final String server, final String virtualHost, final String username,
									  final String password, final String queueName,
									  final MessageHandler<?> messageHandler, final Long timeout,
									  final Integer batchSize) {
		super(createRabbitMQCanalConnector(server, virtualHost, queueName, username, password), messageHandler, timeout,
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

	protected static RabbitMQCanalConnector createRabbitMQCanalConnector(final String server,
																		 final String virtualHost,
																		 final String queueName,
																		 final String username,
																		 final String password) {
		return new RabbitMQCanalConnector(server, virtualHost, queueName, null, null, username, password,
				null, true);
	}

}
