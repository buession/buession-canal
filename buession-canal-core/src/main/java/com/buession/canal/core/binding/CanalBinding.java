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
package com.buession.canal.core.binding;

import com.buession.canal.core.listener.CanalEventListener;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 实体
 *
 * @param <T>
 *        {@link com.buession.canal.annotation.CanalBinding} 类型
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public final class CanalBinding<T> implements Serializable {

	private final static long serialVersionUID = -7514808298435436878L;

	/**
	 * 类型
	 */
	private final Class<T> type;

	/**
	 * {@link com.buession.canal.annotation.CanalBinding} 实体实例
	 */
	private final T instance;

	/**
	 * 指令
	 */
	private final String destination;

	/**
	 * 事件监听器
	 */
	private List<CanalEventListener> listeners;

	/**
	 * 构造函数
	 *
	 * @param type
	 * 		类型
	 * @param instance
	 *        {@link com.buession.canal.annotation.CanalBinding} 实体实例
	 * @param destination
	 * 		指令
	 */
	public CanalBinding(Class<T> type, T instance, String destination) {
		this.type = type;
		this.instance = instance;
		this.destination = destination;
	}

	/**
	 * 返回类型
	 *
	 * @return 类型
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * 返回 {@link com.buession.canal.annotation.CanalBinding} 实体实例
	 *
	 * @return {@link com.buession.canal.annotation.CanalBinding} 实体实例
	 */
	public T getInstance() {
		return instance;
	}

	/**
	 * 返回指令
	 *
	 * @return 指令
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * 返回事件监听器
	 *
	 * @return 事件监听器
	 */
	public List<CanalEventListener> getListeners() {
		return listeners;
	}

	/**
	 * 设置事件监听器
	 *
	 * @param listeners
	 * 		事件监听器
	 */
	public void setListeners(List<CanalEventListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if(obj instanceof CanalBinding){
			CanalBinding<?> that = (CanalBinding<?>) obj;
			return Objects.equals(this.type, that.type);
		}

		return false;
	}

}
