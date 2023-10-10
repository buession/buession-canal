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
	 * 实例清单
	 */
	private final List<Instance> instances;

	private ExecutorService executorService;

	private volatile boolean running = false;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构造函数
	 *
	 * @param instances
	 * 		实例清单
	 */
	public AbstractCanalClient(final List<Instance> instances) {
		Assert.isNull(instances, "The instances cloud not be null");
		this.instances = instances;
	}

	/**
	 * 构造函数
	 *
	 * @param instances
	 * 		实例清单
	 * @param executorService
	 *        {@link ExecutorService} 实例
	 */
	public AbstractCanalClient(final List<Instance> instances, final ExecutorService executorService) {
		this(instances);
		this.executorService = executorService;
	}

	@Override
	public void start() {
		if(executorService == null){
			logger.info("CanalClient starting...");
			process();
		}else{
			logger.info("CanalClient starting with async...");
			executorService.submit(this::process);
		}

		running = true;
	}

	@Override
	public void stop() {
		logger.info("CanalClient stopping...");
		for(Instance instance : instances){
			instance.getAdapterClient().destroy();
		}

		if(executorService != null){
			executorService.shutdown();
		}

		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	protected void process() {
		while(running){
			for(Instance instance : instances){
				instance.getAdapterClient().init();
			}
		}
	}

}
