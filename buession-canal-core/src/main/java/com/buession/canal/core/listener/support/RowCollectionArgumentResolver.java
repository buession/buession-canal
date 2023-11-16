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
package com.buession.canal.core.listener.support;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.MethodParameter;
import com.buession.core.validator.Validate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * {@link CanalEntry.RowData} 集合参数解析器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class RowDataCollectionArgumentResolver implements EventListenerArgumentResolver {

	@Override
	public boolean supports(MethodParameter parameter) {
		if(Collection.class.isAssignableFrom(parameter.getParameterType())){
			Type[] actualTypeArguments =
					((ParameterizedType) parameter.getParameter().getParameterizedType()).getActualTypeArguments();

			if(Validate.isEmpty(actualTypeArguments)){
				return false;
			}

			return Objects.equals(actualTypeArguments[0], CanalEntry.RowData.class);
		}

		return false;
	}

	@Override
	public Object resolve(MethodParameter parameter, final CanalMessage canalMessage) throws Exception {
		if(canalMessage == null || canalMessage.getRowChange() == null){
			return null;
		}

		if(List.class.isAssignableFrom(parameter.getParameterType())){
			return canalMessage.getRowChange().getRowDatasList();
		}else if(Set.class.isAssignableFrom(parameter.getParameterType())){
			return new LinkedHashSet<>(canalMessage.getRowChange().getRowDatasList());
		}else{
			return null;
		}
	}

}
