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
import org.springframework.lang.Nullable;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 实例 {@link com.buession.canal.annotation.CanalEventListener}
 * 方法参数解析器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public interface EventListenerArgumentResolver {

	/**
	 * Whether the given {@linkplain MethodParameter method parameter} is supported by this resolver.
	 *
	 * @param parameter
	 * 		the method parameter to check
	 *
	 * @return true / false
	 */
	boolean supports(final MethodParameter parameter);

	/**
	 * Resolves a method parameter into an argument value from a event listener.
	 *
	 * @param parameter
	 * 		The method parameter to resolve.
	 * @param canalMessage
	 *        {@link CanalMessage}
	 *
	 * @return The resolved argument value, or {@code null} if not resolvable.
	 *
	 * @throws Exception
	 * 		in case of errors with the preparation of argument values
	 */
	@Nullable
	Object resolve(final MethodParameter parameter, final CanalMessage canalMessage) throws Exception;

}
