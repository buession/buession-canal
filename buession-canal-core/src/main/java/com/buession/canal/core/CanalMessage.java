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

import java.util.List;
import java.util.StringJoiner;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalMessage {

	/**
	 * 数据库名
	 */
	private String schemaName;

	/**
	 * 数据表名
	 */
	private String tableName;

	/**
	 * 事件类型
	 */
	private CanalEntry.EventType eventType;

	/**
	 * 是否为 DDL 操作
	 */
	private boolean isDdl;

	private List<CanalEntry.RowData> rowData;

	/**
	 * 返回数据库名
	 *
	 * @return 数据库名
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * 设置数据库名
	 *
	 * @param schemaName
	 * 		数据库名
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * 返回数据表名
	 *
	 * @return 数据表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置数据表名
	 *
	 * @param tableName
	 * 		数据表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public List<CanalEntry.RowData> getRowData() {
		return rowData;
	}

	public void setRowData(List<CanalEntry.RowData> rowData) {
		this.rowData = rowData;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", "[", "]")
				.add("schemaName='" + schemaName + "'")
				.add("tableName='" + tableName + "'")
				.add("eventType=" + eventType)
				.add("rowData=" + rowData)
				.toString();
	}

}
