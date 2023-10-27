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
package com.buession.canal.core.concurrent;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认 Canal 任务线程池执行器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public final class CanalThreadPoolExecutor extends ThreadPoolExecutor {

	/**
	 * 构造函数
	 */
	public CanalThreadPoolExecutor() {
		this(2, 4, 3L, TimeUnit.SECONDS);
	}

	/**
	 * 构造函数
	 *
	 * @param corePoolSize
	 * 		线程池核心线程大小
	 * @param maximumPoolSize
	 * 		线程池最大线程数量
	 * @param keepAliveTime
	 * 		空闲线程存活时间
	 * @param timeUnit
	 * 		keepAliveTime 的计量单位
	 */
	public CanalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit timeUnit) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, new SynchronousQueue<>(),
				new DefaultThreadFactory("canal"), new ThreadPoolExecutor.DiscardPolicy());
	}

}
