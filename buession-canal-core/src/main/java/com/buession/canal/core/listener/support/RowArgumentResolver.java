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
import com.buession.beans.BeanConverter;
import com.buession.beans.BeanUtils;
import com.buession.beans.DefaultBeanConverter;
import com.buession.beans.converters.DatePropertyConverter;
import com.buession.canal.annotation.Row;
import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.MethodParameter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行数据参数解析器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class RowArgumentResolver implements EventListenerArgumentResolver {

	@Override
	public boolean supports(MethodParameter parameter) {
		return parameter.hasAnnotation(Row.class);
	}

	@Override
	public Object resolve(MethodParameter parameter, final CanalMessage canalMessage) throws Exception {
		if(canalMessage == null || canalMessage.getData() == null){
			return null;
		}

		List<CanalEntry.Column> data = canalMessage.getData();
		Map<String, Object> resultMap = new HashMap<>(data.size());

		data.forEach((row)->{
			resultMap.put(row.getName(), row.getValue());
		});

		if(Map.class.isAssignableFrom(parameter.getParameterType())){
			return resultMap;
		}else{
			final DefaultBeanConverter beanConverter = new DefaultBeanConverter();
			final Object target = BeanUtils.instantiateClass(parameter.getParameterType());

			beanConverter.registerConverter(Date.class, new DatePropertyConverter("yyyy-MM-dd HH:mm:ss"));

			return beanConverter.convert(resultMap, target);
		}
	}

}
