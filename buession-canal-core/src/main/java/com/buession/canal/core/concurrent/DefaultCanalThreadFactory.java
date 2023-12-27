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

import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程工厂
 *
 * @author Yong.Teng
 * @see ThreadFactory
 * @since 0.0.1
 */
public class DefaultCanalThreadFactory implements ThreadFactory {

	private final ThreadGroup group;

	private final AtomicInteger threadNumber = new AtomicInteger(0);

	private final String namePrefix;

	public DefaultCanalThreadFactory(final String namePrefix) {
		SecurityManager securityManager = System.getSecurityManager();
		group = securityManager == null ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
		this.namePrefix = namePrefix + "-thread-";
	}

	@Override
	public Thread newThread(@NonNull Runnable runnable) {
		final Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);

		if(thread.getPriority() != Thread.NORM_PRIORITY){
			thread.setPriority(Thread.NORM_PRIORITY);
		}

		thread.setDaemon(true);

		return thread;
	}

}