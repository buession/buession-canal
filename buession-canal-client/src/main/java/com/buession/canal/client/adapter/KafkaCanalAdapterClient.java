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

/**
 * Canal Kafka 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class KafkaCanalAdapterClient extends AbstractCanalMqAdapterClient<KafkaCanalConnector> {

	/**
	 * 构造函数
	 *
	 * @param servers
	 * 		Kafka 地址
	 * @param topic
	 * 		Topic
	 * @param groupId
	 * 		Group ID
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId) {
		this(servers, topic, groupId, null, DEFAULT_FLAT_MESSAGE);
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
	 * @param batchSize
	 * 		批处理条数
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final int batchSize) {
		this(servers, topic, groupId, null, batchSize, DEFAULT_FLAT_MESSAGE);
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
	 * @param flatMessage
	 * 		true / false
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final boolean flatMessage) {
		this(servers, topic, groupId, null, DEFAULT_BATCH_SIZE, flatMessage);
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
	 * @param batchSize
	 * 		批处理条数
	 * @param flatMessage
	 * 		true / false
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final int batchSize, final boolean flatMessage) {
		this(servers, topic, groupId, null, batchSize, flatMessage);
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
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition) {
		this(servers, topic, groupId, partition, DEFAULT_FLAT_MESSAGE);
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
	 * @param batchSize
	 * 		批处理条数
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final int batchSize) {
		this(servers, topic, groupId, partition, batchSize, DEFAULT_FLAT_MESSAGE);
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
	 * @param flatMessage
	 * 		true / false
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final boolean flatMessage) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, flatMessage), flatMessage);
		setDestination(topic);
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
	 * @param batchSize
	 * 		批处理条数
	 * @param flatMessage
	 * 		true / false
	 */
	public KafkaCanalAdapterClient(final String servers, final String topic, final String groupId,
								   final Integer partition, final int batchSize, final boolean flatMessage) {
		super(createKafkaCanalConnector(servers, topic, groupId, partition, batchSize, flatMessage), batchSize,
				flatMessage);
	}

	protected static KafkaCanalConnector createKafkaCanalConnector(final String servers, final String topic,
																   final String groupId, final Integer partition,
																   final boolean flatMessage) {
		return createKafkaCanalConnector(servers, topic, groupId, partition, DEFAULT_BATCH_SIZE, flatMessage);
	}

	protected static KafkaCanalConnector createKafkaCanalConnector(final String servers, final String topic,
																   final String groupId, final Integer partition,
																   final Integer batchSize, final boolean flatMessage) {
		return new KafkaCanalConnector(servers, topic, partition, groupId, batchSize, flatMessage);
	}

}
