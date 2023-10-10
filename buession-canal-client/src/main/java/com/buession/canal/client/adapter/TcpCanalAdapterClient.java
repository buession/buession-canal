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
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.core.validator.Validate;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Canal TCP 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class TcpCanalAdapterClient extends AbstractCanalAdapterClient {

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
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password) {
		super(createCanalConnector(server, zkServers, destination, username, password));
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
	 * @param timeout
	 * 		超时时长
	 * @param timeoutUnit
	 * 		超时时长单位
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final Integer timeout,
								 final TimeUnit timeoutUnit) {
		super(createCanalConnector(server, zkServers, destination, username, password), timeout, timeoutUnit);
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
	 * @param batchSize
	 * 		批处理条数
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final Integer batchSize) {
		super(createCanalConnector(server, zkServers, destination, username, password), batchSize);
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
	 * @param timeout
	 * 		超时时长
	 * @param timeoutUnit
	 * 		超时时长单位
	 * @param batchSize
	 * 		批处理条数
	 */
	public TcpCanalAdapterClient(final String server, final String zkServers, final String destination,
								 final String username, final String password, final Integer timeout,
								 final TimeUnit timeoutUnit, final Integer batchSize) {
		super(createCanalConnector(server, zkServers, destination, username, password), timeout, timeoutUnit,
				batchSize);
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
