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

import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.convert.MessageConverter;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Canal 适配器接口
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public interface CanalAdapterClient {

	int DEFAULT_BATCH_SIZE = 1;

	/**
	 * 返回指令
	 *
	 * @return 指令
	 */
	String getDestination();

	/**
	 * 设置指令
	 *
	 * @param destination
	 * 		指令
	 */
	void setDestination(String destination);

	MessageConverter getMessageConverter();

	void setMessageConverter(MessageConverter messageConverter);

	/**
	 * 初始化客户端
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void init() throws CanalClientException;

	/**
	 * 获取数据，自动进行确认，设置 timeout 时间直到拿到数据为止
	 * <pre>
	 * 该方法返回的条件：
	 *  a. 如果 timeout=0，有多少取多少，不会阻塞等待
	 *  b. 如果 timeout不为0，尝试阻塞对应的超时时间，直到拿到数据就返回
	 * </pre>
	 *
	 * @param timeout
	 * 		超时时长
	 * @param unit
	 * 		超时时长单位
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	List<CanalMessage> getList(Long timeout, TimeUnit unit) throws CanalClientException;

	/**
	 * 获取数据，设置 timeout 时间直到拿到数据为止
	 * <pre>
	 * 该方法返回的条件：
	 *  a. 如果 timeout=0，有多少取多少，不会阻塞等待
	 *  b. 如果 timeout不为0，尝试阻塞对应的超时时间，直到拿到数据就返回
	 * </pre>
	 *
	 * @param timeout
	 * 		超时时长
	 * @param unit
	 * 		超时时长单位
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	List<CanalMessage> getListWithoutAck(Long timeout, TimeUnit unit) throws CanalClientException;

	/**
	 * 进行 batch id 的消费确认，确认之后，小于等于此 batchId 的 Message 都会被确认
	 *
	 * @param batchId
	 * 		Batch Id
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void ack(long batchId) throws CanalClientException;

	/**
	 * 消费确认
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void ack() throws CanalClientException;

	/**
	 * 回滚到未进行 {@link #ack} 的地方，指定回滚具体的 batchId
	 *
	 * @param batchId
	 * 		Batch Id
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void rollback(long batchId) throws CanalClientException;

	/**
	 * 回滚到未进行 {@link #ack} 的地方，下次fetch的时候，可以从最后一个没有 {@link #ack} 的地方开始拿
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void rollback() throws CanalClientException;

	/**
	 * 关闭客户端
	 *
	 * @throws CanalClientException
	 * 		Canal 客户端异常
	 */
	void close() throws CanalClientException;

}
