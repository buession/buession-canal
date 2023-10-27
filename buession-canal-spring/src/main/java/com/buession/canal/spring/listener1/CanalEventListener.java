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
package com.buession.canal.spring.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.core.Callable;
import com.buession.canal.core.Invoke;

import java.lang.reflect.Method;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalEventListener {

	/**
	 * The destination
	 */
	private final String destination;

	/**
	 * 监听的方法和回调
	 */
	private final Invoke invoke;

	/**
	 * 构造函数
	 *
	 * @param destination
	 * 		The destination
	 * @param eventListener
	 * 		数据库操作监听器
	 * @param target
	 * 		目标
	 * @param method
	 * 		监听的方法和节点
	 */
	public CanalEventListener(final String destination,
							  final com.buession.canal.annotation.CanalEventListener eventListener, final Object target,
							  final Method method) {
		this.destination = destination;
		this.invoke = new Invoke(target, method, new DefaultCallable(target, method), eventListener.eventType());
	}

	/**
	 * Return the destination
	 *
	 * @return The destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * 返回监听的方法和回调
	 *
	 * @return 监听的方法和回调
	 */
	public Invoke getInvoke() {
		return invoke;
	}

	private final static class DefaultCallable implements Callable {

		private final Object target;

		private final Method method;

		public DefaultCallable(final Object target, final Method method) {
			this.target = target;
			this.method = method;
		}

		@Override
		public void call(String destination, String schemaName, String tableName, CanalEntry.RowChange rowChange) {
			//callable.call(destination, schemaName, tableName, rowChange);
			System.out.println(destination + ": " + schemaName + ", " + tableName);
			System.out.println(method);
			/*
			try{
				method.invoke(target);
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}catch(InvocationTargetException e){
				e.printStackTrace();
			}

			 */
		}

	}

}
