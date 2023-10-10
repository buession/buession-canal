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
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.canal.client.handler.MessageHandler;
import com.buession.core.validator.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Canal TCP 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class TcpCanalAdapterClient extends AbstractCanalAdapterClient<CanalConnector> {

	private final static Logger logger = LoggerFactory.getLogger(TcpCanalAdapterClient.class);

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		-
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param messageHandler
	 * 		消息处理器
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final MessageHandler<?> messageHandler) {
		super(createCanalConnector(server, zkServers, destination, username, password), messageHandler);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		-
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final MessageHandler<?> messageHandler,
								 final Long timeout) {
		super(createCanalConnector(server, zkServers, destination, username, password), messageHandler, timeout);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		-
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param messageHandler
	 * 		消息处理器
	 * @param batchSize
	 * 		批处理条数
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final MessageHandler<?> messageHandler,
								 final Integer batchSize) {
		super(createCanalConnector(server, zkServers, destination, username, password), messageHandler, batchSize);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		-
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param messageHandler
	 * 		消息处理器
	 * @param timeout
	 * 		超时时长
	 * @param batchSize
	 * 		批处理条数
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final MessageHandler<?> messageHandler,
								 final Long timeout, final Integer batchSize) {
		super(createCanalConnector(server, zkServers, destination, username, password), messageHandler, timeout,
				batchSize);
	}

	@Override
	protected void process() {
		long batchId = 0L;
		try{
			Message message = getConnector().getWithoutAck(getBatchSize(), getTimeout(), TIMEOUT_UNIT);

			if(logger.isDebugEnabled()){
				logger.debug("Receive message = {}", message);
			}

			batchId = message.getId();
			if(message.getId() != -1 && message.getEntries().size() != 0){
				getMessageHandler().handle(message);
			}

			getConnector().ack(batchId);
		}catch(Exception e){
			logger.error("Handle message[batch id: {}] error, rollback", batchId, e);
			getConnector().rollback(batchId);
		}
	}

	protected static CanalConnector createCanalConnector(final String server, final String zkServers,
														 final String destination, final String username,
														 final String password) {
		if(Validate.hasText(zkServers)){
			return CanalConnectors.newClusterConnector(zkServers, destination, username, password);
		}else{
			String[] hostnameAndPort = server.split(":");

			if(hostnameAndPort.length != 2 || Validate.isNumeric(hostnameAndPort[1]) == false){
				throw new CanalClientException("Illegal canal host and port: " + server);
			}

			return CanalConnectors.newSingleConnector(
					new InetSocketAddress(hostnameAndPort[0], Integer.parseInt(hostnameAndPort[1])), destination,
					username, password);
		}
	}

}
