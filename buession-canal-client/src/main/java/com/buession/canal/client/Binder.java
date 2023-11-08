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

import com.buession.canal.client.adapter.CanalAdapterClient;
import com.buession.canal.core.binding.CanalBinding;

import java.io.Serializable;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 实体和 Canal 适配器绑定者
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class Binder implements Serializable {

	private final static long serialVersionUID = -6134832445159441225L;

	/**
	 * {@link com.buession.canal.annotation.CanalBinding} 实体
	 */
	private CanalBinding<?> binding;

	/**
	 * Canal 适配器
	 */
	private CanalAdapterClient adapterClient;

	/**
	 * 超时时长，单位：秒
	 */
	private long timeout;

	/**
	 * 返回 {@link com.buession.canal.annotation.CanalBinding} 实体
	 *
	 * @return {@link com.buession.canal.annotation.CanalBinding} 实体
	 */
	public CanalBinding<?> getBinding() {
		return binding;
	}

	/**
	 * 设置 {@link com.buession.canal.annotation.CanalBinding} 实体
	 *
	 * @param binding
	 *        {@link com.buession.canal.annotation.CanalBinding} 实体
	 */
	public void setBinding(CanalBinding<?> binding) {
		this.binding = binding;
	}

	/**
	 * 返回 Canal 适配器
	 *
	 * @return Canal 适配器
	 */
	public CanalAdapterClient getAdapterClient() {
		return adapterClient;
	}

	/**
	 * 设置 Canal 适配器
	 *
	 * @param adapterClient
	 * 		Canal 适配器
	 */
	public void setAdapterClient(CanalAdapterClient adapterClient) {
		this.adapterClient = adapterClient;
	}

	/**
	 * 返回超时时长
	 *
	 * @return 超时时长，单位：秒
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时时长，单位：秒
	 *
	 * @param timeout
	 * 		超时时长
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
