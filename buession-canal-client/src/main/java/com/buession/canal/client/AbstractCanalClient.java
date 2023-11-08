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

import com.buession.canal.client.handler.DefaultMessageHandler;
import com.buession.canal.client.handler.MessageHandlerFactory;
import com.buession.canal.core.binding.CanalBinding;
import com.buession.canal.core.concurrent.DefaultCanalThreadPoolExecutor;
import com.buession.core.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Canal 客户端抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalClient implements CanalClient {

	/**
	 * {@link Binder} 列表
	 */
	private final List<Binder> binders;

	/**
	 * {@link ExecutorService}
	 */
	private final ExecutorService executor;

	private final MessageHandlerFactory messageHandlerFactory;

	private volatile boolean running = false;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构造函数
	 *
	 * @param binders
	 *        {@link Binder} 列表
	 */
	public AbstractCanalClient(final List<Binder> binders) {
		this(binders, new DefaultCanalThreadPoolExecutor());
	}

	/**
	 * 构造函数
	 *
	 * @param binders
	 *        {@link Binder} 列表
	 *        {@link CanalBinding} 列表
	 * @param executor
	 *        {@link ExecutorService}
	 */
	public AbstractCanalClient(final List<Binder> binders, final ExecutorService executor) {
		Assert.isNull(binders, "The Binder cloud not be null");
		Assert.isNull(executor, "The ExecutorService cloud not be null");
		this.binders = binders;
		this.executor = executor;
		this.messageHandlerFactory = DefaultMessageHandler::new;
	}

	@Override
	public void start() {
		logger.info("CanalClient starting...");

		for(Binder binder : binders){
			process(binder, messageHandlerFactory, executor);
		}

		running = true;
	}

	@Override
	public void stop() {
		logger.info("CanalClient stopping...");
		executor.shutdown();
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	protected abstract void process(final Binder binder, final MessageHandlerFactory messageHandlerFactory,
									final ExecutorService executor);

}
