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

import com.alibaba.otter.canal.client.pulsarmq.PulsarMQCanalConnector;
import com.buession.canal.core.Configuration;

/**
 * Canal PulsarMQ 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class PulsarMQAdapterClient extends AbstractMqAdapterClient<PulsarMQCanalConnector> {

	public final static int DEFAULT_GET_BATCH_TIMEOUT = 30;

	public final static int DEFAULT_BATCH_PROCESS_TIMEOUT = 60;

	public final static int DEFAULT_REDELIVERY_DELAY = 60;

	public final static int DEFAULT_ACK_TIMEOUT = 30;

	public final static boolean DEFAULT_RETRY = true;

	public final static boolean DEFAULT_RETRY_DLQ_UPPERCASE = true;

	public final static int DEFAULT_MAX_REDELIVERY_COUNT = 128;

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName) {
		this(serviceUrl, roleToken, topic, subscriptName, null);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param configuration
	 * 		配置
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final Configuration configuration) {
		this(serviceUrl, roleToken, topic, subscriptName, configuration, DEFAULT_FLAT_MESSAGE);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param flatMessage
	 * 		true / false
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final boolean flatMessage) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, flatMessage), subscriptName,
				null, flatMessage);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param configuration
	 * 		配置
	 * @param flatMessage
	 * 		true / false
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final Configuration configuration,
								 final boolean flatMessage) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, configuration == null ?
						Configuration.DEFAULT_BATCH_SIZE : configuration.getBatchSize(), flatMessage),
				subscriptName, configuration, flatMessage);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param getBatchTimeout
	 * 		-
	 * @param batchProcessTimeout
	 * 		-
	 * @param redeliveryDelay
	 * 		-
	 * @param ackTimeout
	 * 		-
	 * @param isRetry
	 * 		是否重试
	 * @param isRetryDLQUpperCase
	 * 		-
	 * @param maxRedeliveryCount
	 * 		-
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final int getBatchTimeout,
								 final int batchProcessTimeout, final int redeliveryDelay, final int ackTimeout,
								 final boolean isRetry, final boolean isRetryDLQUpperCase,
								 final Integer maxRedeliveryCount) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, getBatchTimeout,
				batchProcessTimeout, redeliveryDelay, ackTimeout, isRetry, isRetryDLQUpperCase,
				maxRedeliveryCount, DEFAULT_FLAT_MESSAGE), subscriptName, DEFAULT_FLAT_MESSAGE);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param getBatchTimeout
	 * 		-
	 * @param batchProcessTimeout
	 * 		-
	 * @param redeliveryDelay
	 * 		-
	 * @param ackTimeout
	 * 		-
	 * @param isRetry
	 * 		是否重试
	 * @param isRetryDLQUpperCase
	 * 		-
	 * @param maxRedeliveryCount
	 * 		-
	 * @param configuration
	 * 		配置
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final int getBatchTimeout,
								 final int batchProcessTimeout, final int redeliveryDelay, final int ackTimeout,
								 final boolean isRetry, final boolean isRetryDLQUpperCase,
								 final Integer maxRedeliveryCount, final Configuration configuration) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, configuration == null ?
						Configuration.DEFAULT_BATCH_SIZE : configuration.getBatchSize(), getBatchTimeout,
				batchProcessTimeout, redeliveryDelay, ackTimeout, isRetry, isRetryDLQUpperCase,
				maxRedeliveryCount, DEFAULT_FLAT_MESSAGE), subscriptName, configuration, DEFAULT_FLAT_MESSAGE);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param getBatchTimeout
	 * 		-
	 * @param batchProcessTimeout
	 * 		-
	 * @param redeliveryDelay
	 * 		-
	 * @param ackTimeout
	 * 		-
	 * @param isRetry
	 * 		是否重试
	 * @param isRetryDLQUpperCase
	 * 		-
	 * @param maxRedeliveryCount
	 * 		-
	 * @param flatMessage
	 * 		true / false
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final int getBatchTimeout,
								 final int batchProcessTimeout, final int redeliveryDelay, final int ackTimeout,
								 final boolean isRetry, final boolean isRetryDLQUpperCase,
								 final Integer maxRedeliveryCount, final boolean flatMessage) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, getBatchTimeout,
				batchProcessTimeout, redeliveryDelay, ackTimeout, isRetry, isRetryDLQUpperCase,
				maxRedeliveryCount, flatMessage), subscriptName, flatMessage);
	}

	/**
	 * 构造函数
	 *
	 * @param serviceUrl
	 * 		PulsarMQ 服务地址
	 * @param roleToken
	 * 		Role Token
	 * @param topic
	 * 		Topic
	 * @param subscriptName
	 * 		订阅名称
	 * @param getBatchTimeout
	 * 		-
	 * @param batchProcessTimeout
	 * 		-
	 * @param redeliveryDelay
	 * 		-
	 * @param ackTimeout
	 * 		-
	 * @param isRetry
	 * 		是否重试
	 * @param isRetryDLQUpperCase
	 * 		-
	 * @param maxRedeliveryCount
	 * 		-
	 * @param configuration
	 * 		配置
	 * @param flatMessage
	 * 		true / false
	 */
	public PulsarMQAdapterClient(final String serviceUrl, final String roleToken, final String topic,
								 final String subscriptName, final int getBatchTimeout,
								 final int batchProcessTimeout, final int redeliveryDelay,
								 final int ackTimeout, final boolean isRetry,
								 final boolean isRetryDLQUpperCase, final Integer maxRedeliveryCount,
								 final Configuration configuration, final boolean flatMessage) {
		super(createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, configuration == null ?
						Configuration.DEFAULT_BATCH_SIZE : configuration.getBatchSize(), getBatchTimeout,
				batchProcessTimeout, redeliveryDelay, ackTimeout, isRetry, isRetryDLQUpperCase,
				maxRedeliveryCount, flatMessage), subscriptName, configuration, flatMessage);
	}

	protected static PulsarMQCanalConnector createPulsarMQCanalConnector(final String serviceUrl,
																		 final String roleToken,
																		 final String topic,
																		 final String subscriptName,
																		 final boolean flatMessage) {
		return createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName,
				Configuration.DEFAULT_BATCH_SIZE, flatMessage);
	}

	protected static PulsarMQCanalConnector createPulsarMQCanalConnector(final String serviceUrl,
																		 final String roleToken,
																		 final String topic,
																		 final String subscriptName,
																		 final Integer batchSize,
																		 final boolean flatMessage) {
		return createPulsarMQCanalConnector(serviceUrl, roleToken, topic, subscriptName, batchSize,
				DEFAULT_GET_BATCH_TIMEOUT, DEFAULT_BATCH_PROCESS_TIMEOUT, DEFAULT_REDELIVERY_DELAY, DEFAULT_ACK_TIMEOUT,
				DEFAULT_RETRY, DEFAULT_RETRY_DLQ_UPPERCASE, DEFAULT_MAX_REDELIVERY_COUNT, flatMessage);
	}

	protected static PulsarMQCanalConnector createPulsarMQCanalConnector(final String serviceUrl,
																		 final String roleToken,
																		 final String topic,
																		 final String subscriptName,
																		 final Integer getBatchTimeout,
																		 final Integer batchProcessTimeout,
																		 final Integer redeliveryDelay,
																		 final Integer ackTimeout,
																		 final Boolean isRetry,
																		 final Boolean isRetryDLQUpperCase,
																		 final Integer maxRedeliveryCount,
																		 final boolean flatMessage) {
		return new PulsarMQCanalConnector(flatMessage, serviceUrl, roleToken, topic, subscriptName,
				Configuration.DEFAULT_BATCH_SIZE, getBatchTimeout, batchProcessTimeout, redeliveryDelay, ackTimeout,
				isRetry, isRetryDLQUpperCase, maxRedeliveryCount);
	}

	protected static PulsarMQCanalConnector createPulsarMQCanalConnector(final String serviceUrl,
																		 final String roleToken,
																		 final String topic,
																		 final String subscriptName,
																		 final Integer batchSize,
																		 final Integer getBatchTimeout,
																		 final Integer batchProcessTimeout,
																		 final Integer redeliveryDelay,
																		 final Integer ackTimeout,
																		 final Boolean isRetry,
																		 final Boolean isRetryDLQUpperCase,
																		 final Integer maxRedeliveryCount,
																		 final boolean flatMessage) {
		return new PulsarMQCanalConnector(flatMessage, serviceUrl, roleToken, topic, subscriptName, batchSize,
				getBatchTimeout, batchProcessTimeout, redeliveryDelay, ackTimeout, isRetry, isRetryDLQUpperCase,
				maxRedeliveryCount);
	}

}
