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
package com.buession.canal.spring.client.factory;

import com.buession.canal.client.CanalClient;
import com.buession.canal.client.CanalContext;
import com.buession.canal.client.dispatcher.Dispatcher;

import java.util.concurrent.ExecutorService;

/**
 * {@link CanalClient} 工厂
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalClientFactory {

	/**
	 * {@link CanalContext}
	 */
	private CanalContext context;

	/**
	 * 分发器
	 */
	private Dispatcher dispatcher;

	/**
	 * {@link ExecutorService}
	 */
	private ExecutorService executor;

	/**
	 * 返回 {@link CanalContext}
	 *
	 * @return {@link CanalContext}
	 */
	public CanalContext getContext() {
		return context;
	}

	/**
	 * 设置 {@link CanalContext}
	 *
	 * @param context
	 *        {@link CanalContext}
	 */
	public void setContext(CanalContext context) {
		this.context = context;
	}

	/**
	 * 返回分发器
	 *
	 * @return 分发器
	 */
	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * 设置分发器
	 *
	 * @param dispatcher
	 * 		分发器
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * 返回 {@link ExecutorService}
	 *
	 * @return {@link ExecutorService}
	 */
	public ExecutorService getExecutor() {
		return executor;
	}

	/**
	 * 设置 {@link ExecutorService}
	 *
	 * @param executor
	 *        {@link ExecutorService}
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
