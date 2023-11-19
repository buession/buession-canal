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
package com.buession.canal.core;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

/**
 * 结果
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class Result implements Serializable {

	private final static long serialVersionUID = 5375957586654168072L;

	/**
	 * ID
	 */
	private long id;

	/**
	 * 消息列表
	 */
	private List<CanalMessage> messages;

	/**
	 * 构造函数
	 */
	public Result() {
	}

	/**
	 * 构造函数
	 *
	 * @param messages
	 * 		消息列表
	 */
	public Result(List<CanalMessage> messages) {
		this(-1, messages);
	}

	/**
	 * 构造函数
	 *
	 * @param id
	 * 		ID
	 * @param messages
	 * 		消息列表
	 */
	public Result(long id, List<CanalMessage> messages) {
		this.id = id;
		this.messages = messages;
	}

	/**
	 * 返回 ID
	 *
	 * @return ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置 ID
	 *
	 * @param id
	 * 		ID
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 返回消息列表
	 *
	 * @return 消息列表
	 */
	public List<CanalMessage> getMessages() {
		return messages;
	}

	/**
	 * 设置消息列表
	 *
	 * @param messages
	 * 		消息列表
	 */
	public void setMessages(List<CanalMessage> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", "Result[", "]")
				.add("id=" + id)
				.add("messages=" + messages)
				.toString();
	}

}
