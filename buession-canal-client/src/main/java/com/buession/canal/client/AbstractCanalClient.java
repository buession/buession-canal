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
package com.buession.canal.client;

import com.buession.canal.client.adapter.AdapterClient;
import com.buession.canal.client.dispatcher.Dispatcher;
import com.buession.canal.core.concurrent.DefaultCanalThreadPoolExecutor;
import com.buession.core.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Canal 客户端抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalClient implements CanalClient {

	/**
	 * {@link CanalContext}
	 */
	private final CanalContext context;

	/**
	 * {@link ExecutorService}
	 */
	private final ExecutorService executor;

	/**
	 * 分发器
	 */
	private final Dispatcher dispatcher;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构造函数
	 *
	 * @param context
	 *        {@link CanalContext}
	 * @param dispatcher
	 * 		分发器
	 */
	public AbstractCanalClient(final CanalContext context, final Dispatcher dispatcher) {
		this(context, dispatcher, new DefaultCanalThreadPoolExecutor());
	}

	/**
	 * 构造函数
	 *
	 * @param context
	 *        {@link CanalContext}
	 * @param dispatcher
	 * 		分发器
	 * @param executor
	 *        {@link ExecutorService}
	 */
	public AbstractCanalClient(final CanalContext context, final Dispatcher dispatcher,
							   final ExecutorService executor) {
		Assert.isNull(context, "The CanalContext is required");
		Assert.isNull(dispatcher, "The Dispatcher is required");
		Assert.isNull(executor, "The ExecutorService is required");
		this.context = context;
		this.dispatcher = dispatcher;
		this.executor = executor;
	}

	@Override
	public void start() {
		logger.info("CanalClient starting...");

		context.getAdapterClients().forEach((adapterClient)->{
			adapterClient.init();
			process(adapterClient, dispatcher, executor);
		});
	}

	@Override
	public void stop() {
		logger.info("CanalClient stopping...");
		context.getAdapterClients().forEach(AdapterClient::close);
		executor.shutdown();
	}

	protected abstract void process(final AdapterClient adapterClient, final Dispatcher dispatcher,
									final ExecutorService executor);

}
