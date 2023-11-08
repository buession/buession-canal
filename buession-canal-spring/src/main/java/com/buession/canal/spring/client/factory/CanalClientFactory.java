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

import com.buession.canal.client.Binder;
import com.buession.canal.client.CanalClient;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * {@link CanalClient} 工厂
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalClientFactory {

	/**
	 * {@link Binder} 列表
	 */
	private List<Binder> binders;

	/**
	 * {@link ExecutorService}
	 */
	private ExecutorService executor;

	/**
	 * 返回 {@link Binder} 列表
	 *
	 * @return {@link Binder} 列表
	 */
	public List<Binder> getBinders() {
		return binders;
	}

	/**
	 * 设置 {@link Binder} 列表
	 *
	 * @param binders
	 *        {@link Binder} 列表
	 */
	public void setBinders(List<Binder> binders) {
		this.binders = binders;
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
