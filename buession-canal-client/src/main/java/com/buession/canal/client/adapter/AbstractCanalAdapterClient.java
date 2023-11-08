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
import com.buession.canal.core.convert.DefaultMessageConverter;
import com.buession.canal.core.convert.MessageConverter;
import com.buession.core.utils.Assert;
import com.buession.core.validator.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Canal 适配器抽象类
 *
 * @param <C>
 * 		Canal 数据操作客户端
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public abstract class AbstractCanalAdapterClient<C extends CanalConnector> implements CanalAdapterClient {

	/**
	 * Canal 数据操作客户端
	 */
	private final C connector;

	/**
	 * 过滤规则
	 */
	private String filter;

	/**
	 * 批处理条数
	 */
	private final int batchSize;

	/**
	 * 指令
	 */
	private String destination;

	private MessageConverter messageConverter = new DefaultMessageConverter();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 */
	public AbstractCanalAdapterClient(final C connector) {
		this(connector, 1);
	}

	/**
	 * 构造函数
	 *
	 * @param connector
	 * 		Canal 数据操作客户端
	 * @param batchSize
	 * 		批处理条数
	 */
	public AbstractCanalAdapterClient(final C connector, final int batchSize) {
		Assert.isNull(connector, "CanalConnector cloud not be null.");
		this.connector = connector;
		this.batchSize = batchSize;
	}

	/**
	 * 返回过滤规则
	 *
	 * @return 过滤规则
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * 设置过滤规则
	 *
	 * @param filter
	 * 		过滤规则
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public String getDestination() {
		return destination;
	}

	@Override
	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	@Override
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public void init() throws CanalClientException {
		connector.connect();

		if(Validate.isBlank(getFilter())){
			connector.subscribe();
		}else{
			connector.subscribe(getFilter());
		}

		// 回滚到未进行 ack 的地方，下次 fetch 时，可以从最后一个没有 ack 的位置获取数据
		connector.rollback();
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
	public void close() throws CanalClientException {
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

	/**
	 * 返回批处理条数
	 *
	 * @return 批处理条数
	 */
	protected int getBatchSize() {
		return batchSize;
	}

	@SuppressWarnings({"unchecked"})
	protected List<CanalMessage> messagesConvert(final Message message) {
		List<CanalMessage> messages = getMessageConverter().convert(message);

		messages.forEach((m)->m.setDestination(getDestination()));

		return messages;
	}

	protected List<CanalMessage> messagesConvert(final List<Message> messages) {
		List<CanalMessage> result = new ArrayList<>(messages.size());

		for(Message message : messages){
			result.addAll(messagesConvert(message));
		}

		return result;
	}

}
