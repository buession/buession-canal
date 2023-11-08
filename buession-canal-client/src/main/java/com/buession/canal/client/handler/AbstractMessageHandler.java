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
package com.buession.canal.client.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.client.Binder;
import com.buession.canal.client.adapter.CanalAdapterClient;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.binding.CanalBinding;
import com.buession.canal.core.listener.CanalEventListener;
import com.buession.canal.core.listener.ParameterMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 信息转换抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractMessageHandler implements MessageHandler {

	private final CanalAdapterClient adapterClient;

	private final CanalBinding<?> binding;

	private final long timeout;

	private volatile boolean running = true;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public AbstractMessageHandler(final Binder binder) {
		this.adapterClient = binder.getAdapterClient();
		this.binding = binder.getBinding();
		this.timeout = binder.getTimeout();
		adapterClient.init();
	}

	@Override
	public void run() {
		while(running){
			try{
				List<CanalMessage> messages = adapterClient.getListWithoutAck(timeout, TimeUnit.SECONDS);

				for(CanalMessage message : messages){
					distributeEvent(message);
				}

				adapterClient.ack();
			}catch(Exception e){
				logger.error("Message handle error", e);
			}
		}

		running = false;
	}

	protected CanalBinding<?> getBinding() {
		return binding;
	}

	protected Object[] getInvokeArgs(final CanalEventListener eventListener, final CanalMessage message) {
		return Arrays.stream(eventListener.getParameterMappings()).map(convertParameter(message)).toArray();
	}

	protected abstract void distributeEvent(final CanalMessage message);

	protected Function<ParameterMapping, Object> convertParameter(final CanalMessage message) {
		return (pm)->{
			if(pm.getType().getClass().isAssignableFrom(CanalEntry.RowData.class)){
				return message.getRowData().get(0);
			}else{
				return null;
			}
		};
	}

}
