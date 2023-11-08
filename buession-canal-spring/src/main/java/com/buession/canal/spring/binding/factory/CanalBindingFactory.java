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
package com.buession.canal.spring.binding.factory;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 工厂
 *
 * @param <T>
 *        {@link com.buession.canal.annotation.CanalBinding} 类型
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBindingFactory<T> {

	/**
	 * 指令
	 */
	private String destination;

	/**
	 * {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	private Class<T> bindingType;

	/**
	 * 构造函数
	 */
	public CanalBindingFactory() {
	}

	/**
	 * 构造函数
	 *
	 * @param bindingType
	 *        {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public CanalBindingFactory(Class<T> bindingType) {
		this.bindingType = bindingType;
	}

	/**
	 * 构造函数
	 *
	 * @param destination
	 * 		指令
	 * @param bindingType
	 *        {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public CanalBindingFactory(String destination, Class<T> bindingType) {
		this.destination = destination;
		this.bindingType = bindingType;
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
	 * 设置指令
	 *
	 * @param destination
	 * 		指令
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * 返回 {@link com.buession.canal.annotation.CanalBinding} 类型
	 *
	 * @return {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public Class<T> getBindingType() {
		return bindingType;
	}

	/**
	 * 设置 {@link com.buession.canal.annotation.CanalBinding} 类型
	 *
	 * @param bindingType
	 *        {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public void setBindingType(Class<T> bindingType) {
		this.bindingType = bindingType;
	}

}
