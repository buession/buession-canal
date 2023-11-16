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

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 消息
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalMessage implements Serializable {

	private final static long serialVersionUID = -5272311798653766460L;

	/**
	 * 指令
	 */
	private String destination;

	/**
	 * 数据库信息
	 */
	private Table table;

	/**
	 * 事件类型
	 */
	private CanalEntry.EntryType entryType;

	/**
	 * 事件类型
	 */
	private CanalEntry.EventType eventType;

	/**
	 * 是否为 DDL 操作
	 */
	private boolean isDdl;

	private CanalEntry.Header header;

	private CanalEntry.RowChange rowChange;

	private Object data;

	/**
	 * 返回指令
	 *
	 * @return 指令
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * 设置指令
	 *
	 * @param destination
	 * 		指令
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * 返回数据库信息
	 *
	 * @return 数据库信息
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * 设置数据库信息
	 *
	 * @param table
	 * 		数据库信息
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	public CanalEntry.EntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(CanalEntry.EntryType entryType) {
		this.entryType = entryType;
	}

	/**
	 * 返回事件类型
	 *
	 * @return 事件类型
	 */
	public CanalEntry.EventType getEventType() {
		return eventType;
	}

	/**
	 * 设置事件类型
	 *
	 * @param eventType
	 * 		事件类型
	 */
	public void setEventType(CanalEntry.EventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * 返回是否为 DDL 操作
	 *
	 * @return true / false
	 */
	public boolean isDdl() {
		return isDdl;
	}

	/**
	 * 设置是否为 DDL 操作
	 *
	 * @param isDdl
	 * 		DDL 操作
	 */
	public void setDdl(boolean isDdl) {
		this.isDdl = isDdl;
	}

	public CanalEntry.Header getHeader() {
		return header;
	}

	public void setHeader(CanalEntry.Header header) {
		this.header = header;
	}

	public CanalEntry.RowChange getRowChange() {
		return rowChange;
	}

	public void setRowChange(CanalEntry.RowChange rowChange) {
		this.rowChange = rowChange;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", "CanalMessage[", "]")
				.add("destination='" + destination + "'")
				.add("table=" + table)
				.add("entryType=" + entryType)
				.add("eventType=" + eventType)
				.add("isDdl=" + isDdl)
				.add("header=" + header)
				.add("rowChange=" + rowChange)
				.toString();
	}

}
