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

import com.alibaba.otter.canal.client.CanalConnector;
import com.buession.canal.client.handler.MessageHandler;
import com.buession.core.utils.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Canal 适配器抽象类
 *
 * @param <C>
 * 		Canal 数据操作客户端
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalAdapterClient<C extends CanalConnector> implements CanalAdapterClient {

	protected final static TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

	/**
	 * Canal 数据操作客户端
	 */
	private final C connector;

	/**
	 * 消息处理器
	 */
	private MessageHandler<?> messageHandler;

	/**
	 * 超时时长，单位：秒
	 */
	private final Long timeout;

	/**
	 * 批处理条数
	 */
	private final Integer batchSize;

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param messageHandler
	 * 		消息处理器
	 */
	public AbstractCanalAdapterClient(final C connector, final MessageHandler<?> messageHandler) {
		this(connector, messageHandler, 1L, 1);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 */
	public AbstractCanalAdapterClient(final C connector, final MessageHandler<?> messageHandler, final Long timeout) {
		this(connector, messageHandler, timeout, 1);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param messageHandler
	 * 		消息处理器
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalAdapterClient(final C connector, final MessageHandler<?> messageHandler,
									  final Integer batchSize) {
		this(connector, messageHandler, 1L, batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalAdapterClient(final C connector, final MessageHandler<?> messageHandler, final Long timeout,
									  final Integer batchSize) {
		Assert.isNull(connector, "CanalConnector cloud not be null.");
		this.connector = connector;
		this.messageHandler = messageHandler;
		this.timeout = timeout;
		this.batchSize = batchSize;
	}

	/**
	 * 返回 Canal 数据操作客户端
	 *
	 * @return Canal 数据操作客户端
	 *
	 * @see CanalConnector
	 */
	public C getConnector() {
		return connector;
	}

	/**
	 * 返回消息处理器
	 *
	 * @return 消息处理器
	 */
	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	/**
	 * 返回超时时长
	 *
	 * @return 超时时长
	 */
	public Long getTimeout() {
		return timeout;
	}

	/**
	 * 返回批处理条数
	 *
	 * @return 批处理条数
	 */
	public Integer getBatchSize() {
		return batchSize;
	}

	@Override
	public void init() {
		connector.connect();
		connector.subscribe();
		process();
	}

	@Override
	public void destroy() {
		connector.unsubscribe();
		connector.disconnect();
	}

	protected abstract void process();

}
