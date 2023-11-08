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

import com.alibaba.otter.canal.client.rocketmq.RocketMQCanalConnector;

/**
 * Canal RocketMQ 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class RocketMQCanalAdapterClient extends AbstractCanalMqAdapterClient<RocketMQCanalConnector> {

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final Boolean enableMessageTrace, final String customizedTraceTopic,
									  final String accessChannel) {
		this(nameServer, topic, groupId, null, enableMessageTrace, customizedTraceTopic, accessChannel);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param batchSize
	 * 		批处理条数
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final Boolean enableMessageTrace, final String customizedTraceTopic,
									  final String accessChannel, final int batchSize) {
		this(nameServer, topic, groupId, null, enableMessageTrace, customizedTraceTopic, accessChannel, batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param flatMessage
	 * 		true / false
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final Boolean enableMessageTrace, final String customizedTraceTopic,
									  final String accessChannel, final boolean flatMessage) {
		this(nameServer, topic, groupId, null, enableMessageTrace, customizedTraceTopic, accessChannel, flatMessage);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param batchSize
	 * 		批处理条数
	 * @param flatMessage
	 * 		true / false
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final Boolean enableMessageTrace, final String customizedTraceTopic,
									  final String accessChannel, final int batchSize, final boolean flatMessage) {
		this(nameServer, topic, groupId, null, enableMessageTrace, customizedTraceTopic, accessChannel, batchSize,
				flatMessage);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param namespace
	 * 		名称空间
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final String namespace, final Boolean enableMessageTrace,
									  final String customizedTraceTopic, final String accessChannel) {
		super(createRocketMQCanalConnector(nameServer, topic, groupId, namespace, enableMessageTrace,
				customizedTraceTopic, accessChannel, DEFAULT_FLAT_MESSAGE));
		setDestination(topic);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param namespace
	 * 		名称空间
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param batchSize
	 * 		批处理条数
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final String namespace, final Boolean enableMessageTrace,
									  final String customizedTraceTopic, final String accessChannel,
									  final int batchSize) {
		super(createRocketMQCanalConnector(nameServer, topic, groupId, namespace, batchSize, enableMessageTrace,
				customizedTraceTopic, accessChannel, DEFAULT_FLAT_MESSAGE), batchSize);
		setDestination(topic);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param namespace
	 * 		名称空间
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param flatMessage
	 * 		true / false
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final String namespace, final Boolean enableMessageTrace,
									  final String customizedTraceTopic, final String accessChannel,
									  final boolean flatMessage) {
		super(createRocketMQCanalConnector(nameServer, topic, groupId, namespace, enableMessageTrace,
				customizedTraceTopic, accessChannel, flatMessage), flatMessage);
		setDestination(topic);
	}

	/**
	 * 构造函数
	 *
	 * @param nameServer
	 * 		RocketMQ NameServer 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 * @param namespace
	 * 		名称空间
	 * @param enableMessageTrace
	 * 		是否启用消息跟踪
	 * @param customizedTraceTopic
	 * 		消息轨迹数据 Topic
	 * @param accessChannel
	 * 		-
	 * @param batchSize
	 * 		批处理条数
	 * @param flatMessage
	 * 		true / false
	 */
	public RocketMQCanalAdapterClient(final String nameServer, final String topic, final String groupId,
									  final String namespace, final Boolean enableMessageTrace,
									  final String customizedTraceTopic, final String accessChannel,
									  final int batchSize, final boolean flatMessage) {
		super(createRocketMQCanalConnector(nameServer, topic, groupId, namespace, batchSize, enableMessageTrace,
				customizedTraceTopic, accessChannel, flatMessage), batchSize, flatMessage);
		setDestination(topic);
	}

	protected static RocketMQCanalConnector createRocketMQCanalConnector(final String nameServer,
																		 final String topic,
																		 final String groupName,
																		 final String namespace,
																		 final Boolean enableMessageTrace,
																		 final String customizedTraceTopic,
																		 final String accessChannel,
																		 final boolean flatMessage) {
		return new RocketMQCanalConnector(nameServer, topic, groupName, null, null, DEFAULT_BATCH_SIZE, flatMessage,
				enableMessageTrace, customizedTraceTopic, accessChannel, namespace);
	}

	protected static RocketMQCanalConnector createRocketMQCanalConnector(final String nameServer,
																		 final String topic,
																		 final String groupName,
																		 final String namespace,
																		 final Integer batchSize,
																		 final Boolean enableMessageTrace,
																		 final String customizedTraceTopic,
																		 final String accessChannel,
																		 final boolean flatMessage) {
		return new RocketMQCanalConnector(nameServer, topic, groupName, null, null, batchSize, flatMessage,
				enableMessageTrace, customizedTraceTopic, accessChannel, namespace);
	}

}
