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
import com.alibaba.otter.canal.client.impl.ClusterCanalConnector;
import com.alibaba.otter.canal.client.impl.SimpleCanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.Configuration;
import com.buession.core.converter.mapper.PropertyMapper;
import com.buession.core.utils.StringUtils;
import com.buession.core.validator.Validate;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Canal TCP 适配器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class TcpAdapterClient extends AbstractAdapterClient<CanalConnector> {

	public final static int DEFAULT_PORT = 1111;

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		指令
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 */
	public TcpAdapterClient(final String server, final String zkServers, final String destination,
							final String username, final String password) {
		super(createCanalConnector(server, zkServers, destination, username, password), destination);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		指令
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param configuration
	 * 		配置
	 */
	public TcpAdapterClient(final String server, final String zkServers, final String destination,
							final String username, final String password, final Configuration configuration) {
		super(createCanalConnector(server, zkServers, destination, username, password), destination, configuration);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		指令
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param soTimeout
	 * 		读取超时
	 * @param idleTimeout
	 * 		连接池超时
	 */
	public TcpAdapterClient(final String server, final String zkServers, final String destination,
							final String username, final String password, final Integer soTimeout,
							final Integer idleTimeout) {
		super(createCanalConnector(server, zkServers, destination, username, password, soTimeout, idleTimeout),
				destination);
	}

	/**
	 * 构造函数
	 *
	 * @param server
	 * 		主机地址
	 * @param zkServers
	 * 		Zookeeper 主机地址
	 * @param destination
	 * 		指令
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 * @param configuration
	 * 		配置
	 * @param soTimeout
	 * 		读取超时
	 * @param idleTimeout
	 * 		连接池超时
	 */
	public TcpAdapterClient(final String server, final String zkServers, final String destination,
							final String username, final String password, final Configuration configuration,
							final Integer soTimeout, final Integer idleTimeout) {
		super(createCanalConnector(server, zkServers, destination, username, password, soTimeout, idleTimeout),
				destination, configuration);
	}

	@Override
	public List<CanalMessage> getList(Long timeout, TimeUnit unit) throws CanalClientException {
		Message message = getConnector().get(configuration.getBatchSize(), timeout, unit);
		return messagesConvert(message);
	}

	@Override
	public List<CanalMessage> getListWithoutAck(Long timeout, TimeUnit unit) throws CanalClientException {
		Message message = getConnector().getWithoutAck(configuration.getBatchSize(), timeout, unit);
		return messagesConvert(message);
	}

	protected static CanalConnector createCanalConnector(final String server, final String zkServers,
														 final String destination, final String username,
														 final String password) {
		return createCanalConnector(server, zkServers, destination, username, password, null, null);
	}

	protected static CanalConnector createCanalConnector(final String server, final String zkServers,
														 final String destination, final String username,
														 final String password, final Integer soTimeout,
														 final Integer idleTimeout) {
		PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

		if(Validate.hasText(zkServers)){
			ClusterCanalConnector canalConnector =
					(ClusterCanalConnector) CanalConnectors.newClusterConnector(zkServers, destination,
							username, password);

			propertyMapper.from(soTimeout).to(canalConnector::setSoTimeout);
			propertyMapper.from(idleTimeout).to(canalConnector::setIdleTimeout);

			return canalConnector;
		}else{
			String[] servers = StringUtils.split(server, ',');

			if(servers.length == 1){
				SocketAddress address = createSocketAddressFromHostAndPort(servers[0]);
				SimpleCanalConnector canalConnector =
						(SimpleCanalConnector) CanalConnectors.newSingleConnector(address, destination,
								username, password);

				propertyMapper.from(soTimeout).to(canalConnector::setSoTimeout);
				propertyMapper.from(idleTimeout).to(canalConnector::setIdleTimeout);

				return canalConnector;
			}else{
				List<SocketAddress> serverSocketAddresses = Stream.of(servers).map(
						TcpAdapterClient::createSocketAddressFromHostAndPort).collect(Collectors.toList());

				ClusterCanalConnector canalConnector =
						(ClusterCanalConnector) CanalConnectors.newClusterConnector(serverSocketAddresses, destination,
								username, password);

				propertyMapper.from(soTimeout).to(canalConnector::setSoTimeout);
				propertyMapper.from(idleTimeout).to(canalConnector::setIdleTimeout);

				return canalConnector;
			}
		}
	}

	protected static SocketAddress createSocketAddressFromHostAndPort(final String hostAndPort) {
		String[] hostnameAndPort = StringUtils.split(hostAndPort, ':');

		if(hostnameAndPort.length == 1){
			return new InetSocketAddress(hostnameAndPort[0], DEFAULT_PORT);
		}else if(hostnameAndPort.length == 2 && Validate.isNumeric(hostnameAndPort[1])){
			return new InetSocketAddress(hostnameAndPort[0], Integer.parseInt(hostnameAndPort[1]));
		}else{
			throw new CanalClientException("Illegal canal server host and port: " + hostAndPort);
		}
	}

}
