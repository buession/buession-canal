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

import com.buession.core.utils.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 数据库信息
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class Table implements Serializable {

	private final static long serialVersionUID = 8941302053989524508L;

	/**
	 * 表名
	 */
	private final String name;

	/**
	 * 数据库名
	 */
	private final String schema;

	/**
	 * 构造函数
	 *
	 * @param name
	 * 		表名
	 * @param schema
	 * 		数据库名
	 */
	public Table(final String name, final String schema) {
		this.name = name;
		this.schema = schema;
	}

	/**
	 * 返回表名
	 *
	 * @return 表名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回数据库名
	 *
	 * @return 数据库名
	 */
	public String getSchema() {
		return schema;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, schema);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if(obj instanceof Table){
			Table that = (Table) obj;
			return StringUtils.equalsIgnoreCase(this.schema, that.schema) &&
					StringUtils.equalsIgnoreCase(this.name, that.name);
		}

		return false;
	}

}
