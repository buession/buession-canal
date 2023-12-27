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
package com.buession.canal.client.adapter;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.Configuration;
import com.buession.canal.core.convert.DefaultMessageConverter;
import com.buession.canal.core.convert.MessageConverter;
import com.buession.core.utils.Assert;
import com.buession.core.validator.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Canal 适配器抽象类
 *
 * @param <C>
 * 		Canal 数据连接器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractAdapterClient<C extends CanalConnector> implements AdapterClient {

	/**
	 * 配置
	 */
	protected Configuration configuration;

	/**
	 * Canal 数据操作客户端
	 */
	private final C connector;

	/**
	 * 消息转换器
	 */
	@SuppressWarnings({"rawtypes"})
	private MessageConverter messageConverter = new DefaultMessageConverter();

	/**
	 * 是否在运行
	 *
	 * @since 0.0.2
	 */
	private volatile boolean running = false;

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 */
	public AbstractAdapterClient(final C connector, final String destination) {
		this(connector, destination, new Configuration());
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param destination
	 * 		指令
	 * @param configuration
	 * 		配置
	 */
	public AbstractAdapterClient(final C connector, final String destination, final Configuration configuration) {
		Assert.isNull(connector, "CanalConnector cloud not be null.");
		this.connector = connector;
		this.configuration = Optional.ofNullable(configuration).orElse(new Configuration());
		this.configuration.setDestination(destination);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public void init() throws CanalClientException {
		connector.connect();

		if(Validate.isBlank(configuration.getFilter())){
			connector.subscribe();
		}else{
			connector.subscribe(configuration.getFilter());
		}

		// 回滚到未进行 ack 的地方，下次 fetch 时，可以从最后一个没有 ack 的位置获取数据
		connector.rollback();
		running = true;
	}

	@Override
	public void ack(long batchId) throws CanalClientException {
		connector.ack(batchId);
	}

	@Override
	public void ack() throws CanalClientException {

	}

	@Override
	public void rollback(long batchId) throws CanalClientException {
		connector.rollback(batchId);
	}

	@Override
	public void rollback() throws CanalClientException {
		connector.rollback();
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void close() throws CanalClientException {
		running = false;
		connector.unsubscribe();
		connector.disconnect();
	}

	/**
	 * 返回 Canal 数据操作客户端
	 *
	 * @return Canal 数据操作客户端
	 *
	 * @see CanalConnector
	 */
	protected C getConnector() {
		return connector;
	}

	@SuppressWarnings({"unchecked"})
	protected List<CanalMessage> messagesConvert(final Message message) {
		return getMessageConverter().convert(configuration.getDestination(), message);
	}

	protected List<CanalMessage> messagesConvert(final List<Message> messages) {
		List<CanalMessage> result = new ArrayList<>(messages.size());

		for(Message message : messages){
			result.addAll(messagesConvert(message));
		}

		return result;
	}

}
