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

import com.buession.canal.client.consumer.CanalConsumer;
import com.buession.canal.core.convert.MessageTransponder;

import java.util.concurrent.TimeUnit;

/**
 * Canal 适配器接口
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public interface CanalAdapterClient {

	TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

	int DEFAULT_BATCH_SIZE = 1;

	/**
	 * 返回信息转换器接口
	 *
	 * @return 信息转换器接口
	 */
	MessageTransponder getMessageTransponder();

	/**
	 * 设置信息转换器接口
	 *
	 * @param messageTransponder
	 * 		信息转换器接口
	 */
	void setMessageTransponder(MessageTransponder messageTransponder);

	/**
	 * 初始化客户端
	 */
	void init();

	/**
	 * 数据处理
	 *
	 * @param consumer
	 * 		Canal 消费者
	 */
	void process(CanalConsumer consumer);

	/**
	 * 销毁客户端
	 */
	void destroy();

}
