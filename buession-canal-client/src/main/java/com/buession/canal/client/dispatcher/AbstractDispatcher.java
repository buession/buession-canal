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
package com.buession.canal.client.dispatcher;

import com.buession.canal.client.adapter.AdapterClient;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.Configuration;
import com.buession.canal.core.Result;
import com.buession.canal.core.listener.EventListenerMethod;
import com.buession.canal.core.listener.EventListenerRegistry;
import com.buession.canal.core.listener.support.DestinationArgumentResolver;
import com.buession.canal.core.listener.support.EntryTypeArgumentResolver;
import com.buession.canal.core.listener.support.EventListenerArgumentResolver;
import com.buession.canal.core.listener.support.EventListenerArgumentResolverComposite;
import com.buession.canal.core.listener.support.EventTypeArgumentResolver;
import com.buession.canal.core.listener.support.HeaderArgumentResolver;
import com.buession.canal.core.listener.support.RowArgumentResolver;
import com.buession.canal.core.listener.support.RowChangeArgumentResolver;
import com.buession.canal.core.listener.support.RowDataArgumentResolver;
import com.buession.canal.core.listener.support.RowDataArrayArgumentResolver;
import com.buession.canal.core.listener.support.RowDataCollectionArgumentResolver;
import com.buession.canal.core.listener.support.SchemaArgumentResolver;
import com.buession.canal.core.listener.support.TableArgumentResolver;
import com.buession.core.builder.ListBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分发器抽象类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractDispatcher implements Dispatcher {

	private final EventListenerRegistry eventListenerRegistry = new EventListenerRegistry();

	private final EventListenerArgumentResolverComposite argumentResolvers = new EventListenerArgumentResolverComposite();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public AbstractDispatcher() {
		argumentResolvers.addResolvers(getDefaultArgumentResolvers());
	}

	public EventListenerRegistry getEventListenerRegistry() {
		return eventListenerRegistry;
	}

	@Override
	public void dispatch(AdapterClient adapterClient) {
		Configuration configuration = adapterClient.getConfiguration();

		try{
			Result result = adapterClient.getListWithoutAck(configuration.getTimeout().toMillis(),
					TimeUnit.MILLISECONDS);

			if(logger.isDebugEnabled()){
				logger.debug("Return {} messages.", result == null ? 0 : result.getMessages().size());
			}

			if(result != null && result.getMessages() != null){
				for(CanalMessage message : result.getMessages()){
					doDispatch(message);
				}
			}

			if(result != null && result.getId() > -1){
				adapterClient.ack(result.getId());
			}else{
				adapterClient.ack();
			}
		}catch(Exception e){
			logger.error("Message handle error", e);
		}
	}

	protected void doDispatch(final CanalMessage canalMessage) throws Exception {
		EventListenerMethod method = findMethod(canalMessage);

		if(method == null){
			return;
		}

		method.setArgumentResolvers(argumentResolvers);
		method.invoke(canalMessage);
	}

	protected abstract EventListenerMethod findMethod(final CanalMessage canalMessage);

	protected static List<EventListenerArgumentResolver> getDefaultArgumentResolvers() {
		return ListBuilder.<EventListenerArgumentResolver>create(11)
				.add(new DestinationArgumentResolver())
				.add(new EntryTypeArgumentResolver())
				.add(new EventTypeArgumentResolver())
				.add(new HeaderArgumentResolver())
				.add(new RowChangeArgumentResolver())
				.add(new RowDataArgumentResolver())
				.add(new RowDataCollectionArgumentResolver())
				.add(new RowDataArrayArgumentResolver())
				.add(new RowArgumentResolver())
				.add(new SchemaArgumentResolver())
				.add(new TableArgumentResolver())
				.build();
	}

}
