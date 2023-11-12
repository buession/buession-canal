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

import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.MethodParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class EventListenerArgumentResolverComposite implements EventListenerArgumentResolver {

	private final List<EventListenerArgumentResolver> argumentResolvers = new ArrayList<>();

	private final Map<MethodParameter, EventListenerArgumentResolver> argumentResolverCache =
			new ConcurrentHashMap<>(256);

	public void addResolver(EventListenerArgumentResolver resolver) {
		argumentResolvers.add(resolver);
	}

	public void addResolvers(EventListenerArgumentResolver... resolvers) {
		if(resolvers != null){
			Collections.addAll(argumentResolvers, resolvers);
		}
	}

	public void addResolvers(List<? extends EventListenerArgumentResolver> resolvers) {
		if(resolvers != null){
			argumentResolvers.addAll(resolvers);
		}
	}

	public List<EventListenerArgumentResolver> getResolvers() {
		return Collections.unmodifiableList(argumentResolvers);
	}

	@Override
	public boolean supports(final MethodParameter parameter) {
		return getArgumentResolver(parameter) != null;
	}

	@Override
	public Object resolve(final MethodParameter parameter, final CanalMessage canalMessage) throws Exception {
		EventListenerArgumentResolver resolver = getArgumentResolver(parameter);
		if(resolver == null){
			throw new IllegalArgumentException("Unsupported parameter type [" +
					parameter.getType().getName() + "]. supports should be called first.");
		}

		return resolver.resolve(parameter, canalMessage);
	}

	private EventListenerArgumentResolver getArgumentResolver(final MethodParameter parameter) {
		EventListenerArgumentResolver result = argumentResolverCache.get(parameter);

		if(result == null){
			for(EventListenerArgumentResolver resolver : argumentResolvers){
				if(resolver.supports(parameter)){
					result = resolver;
					argumentResolverCache.put(parameter, result);
					break;
				}
			}
		}

		return result;
	}

}
