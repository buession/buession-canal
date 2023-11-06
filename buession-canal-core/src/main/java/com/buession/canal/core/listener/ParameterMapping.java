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
package com.buession.canal.core.listener;

import com.buession.canal.core.ParameterType;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class ParameterMapping implements Serializable {

	private final static long serialVersionUID = -3360650251060687317L;

	private String property;

	private Class<?> type = Object.class;

	private ParameterType parameterType;

	private ParameterMapping() {
	}

	public ParameterMapping(String property, Class<?> type, ParameterType parameterType) {
		this.property = property;
		this.type = type;
		this.parameterType = parameterType;
	}

	public String getProperty() {
		return property;
	}

	public Class<?> getType() {
		return type;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(property, type, parameterType);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if(obj instanceof ParameterMapping){
			ParameterMapping that = (ParameterMapping) obj;
			return Objects.equals(property, that.getProperty()) && Objects.equals(type, that.getType()) &&
					Objects.equals(parameterType, that.getParameterType());
		}

		return false;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", "ParameterMapping[", "]")
				.add("property='" + property + "'")
				.add("type=" + type)
				.add("parameterType=" + parameterType)
				.toString();
	}

}
