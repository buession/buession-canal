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
import com.buession.core.utils.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Canal 适配器抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalAdapterClient implements CanalAdapterClient {

	/**
	 * Canal 数据操作客户端
	 */
	private final CanalConnector connector;

	/**
	 * 超时时长
	 */
	private final Integer timeout;

	/**
	 * 超时时长单位
	 */
	private final TimeUnit timeoutUnit;

	/**
	 * 批处理条数
	 */
	private final Integer batchSize;

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 */
	public AbstractCanalAdapterClient(final CanalConnector connector) {
		this(connector, 1, TimeUnit.SECONDS, 1);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param timeout
	 * 		超时时长
	 * @param timeoutUnit
	 * 		超时时长单位
	 */
	public AbstractCanalAdapterClient(final CanalConnector connector, final Integer timeout,
									  final TimeUnit timeoutUnit) {
		this(connector, timeout, timeoutUnit, 1);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalAdapterClient(final CanalConnector connector, final Integer batchSize) {
		this(connector, 1, TimeUnit.SECONDS, batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param timeout
	 * 		超时时长
	 * @param timeoutUnit
	 * 		超时时长单位
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalAdapterClient(final CanalConnector connector, final Integer timeout, final TimeUnit timeoutUnit,
									  final Integer batchSize) {
		Assert.isNull(connector, "CanalConnector cloud not be null.");
		this.connector = connector;
		this.timeout = timeout;
		this.timeoutUnit = timeoutUnit;
		this.batchSize = batchSize;
	}

	/**
	 * 返回 Canal 数据操作客户端
	 *
	 * @return Canal 数据操作客户端
	 *
	 * @see CanalConnector
	 */
	public CanalConnector getConnector() {
		return connector;
	}

	/**
	 * 返回超时时长
	 *
	 * @return 超时时长
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * 返回超时时长单位
	 *
	 * @return 超时时长单位
	 */
	public TimeUnit getTimeoutUnit() {
		return timeoutUnit;
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

	}

	@Override
	public void destroy() {

	}
}
