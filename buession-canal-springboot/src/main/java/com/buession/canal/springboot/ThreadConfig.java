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
package com.buession.canal.springboot;

import com.buession.core.concurrent.ThreadPolicy;

import java.time.Duration;

/**
 * 线程池配置
 *
 * @author Yong.Teng
 * @since 1.0.0
 */
public class ThreadConfig {

	/**
	 * 线程名称前缀
	 */
	private String namePrefix = "canal-execute";

	/**
	 * 线程池核心线程大小
	 */
	private Integer corePoolSize;

	/**
	 * 线程池最大线程数量
	 */
	private Integer maximumPoolSize;

	/**
	 * 空闲线程存活时间
	 */
	private Duration keepAliveTime = Duration.ZERO;

	/**
	 * 饱和策略
	 */
	private ThreadPolicy policy = ThreadPolicy.DISCARD;

	/**
	 * 返回线程名称前缀
	 *
	 * @return 线程名称前缀
	 */
	public String getNamePrefix() {
		return namePrefix;
	}

	/**
	 * 设置线程名称前缀
	 *
	 * @param namePrefix
	 * 		线程名称前缀
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * 返回线程池核心线程大小
	 *
	 * @return 线程池核心线程大小
	 */
	public Integer getCorePoolSize() {
		return corePoolSize;
	}

	/**
	 * 设置线程池核心线程大小
	 *
	 * @param corePoolSize
	 * 		线程池核心线程大小
	 */
	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/**
	 * 返回线程池最大线程数量
	 *
	 * @return 线程池最大线程数量
	 */
	public Integer getMaximumPoolSize() {
		return maximumPoolSize;
	}

	/**
	 * 设置线程池最大线程数量
	 *
	 * @param maximumPoolSize
	 * 		线程池最大线程数量
	 */
	public void setMaximumPoolSize(Integer maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	/**
	 * 返回空闲线程存活时间
	 *
	 * @return 空闲线程存活时间
	 */
	public Duration getKeepAliveTime() {
		return keepAliveTime;
	}

	/**
	 * 设置空闲线程存活时间
	 *
	 * @param keepAliveTime
	 * 		空闲线程存活时间
	 */
	public void setKeepAliveTime(Duration keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	/**
	 * 返回饱和策略
	 *
	 * @return 饱和策略
	 */
	public ThreadPolicy getPolicy() {
		return policy;
	}

	/**
	 * 设置饱和策略
	 *
	 * @param policy
	 * 		饱和策略
	 */
	public void setPolicy(ThreadPolicy policy) {
		this.policy = policy;
	}

}
