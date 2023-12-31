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

import com.buession.canal.core.CanalMessage;
import com.buession.canal.core.listener.support.EventListenerArgumentResolverComposite;
import com.buession.core.utils.Assert;
import com.buession.core.validator.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * {@link com.buession.canal.annotation.CanalEventListener} 方法
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public final class EventListenerMethod {

	private final static Object[] EMPTY_ARGS = new Object[0];

	private EventListenerArgumentResolverComposite argumentResolvers;

	private final Object target;

	private final Class<?> targetType;

	private final Method method;

	private final Method bridgedMethod;

	private final MethodParameter[] parameters;

	private final static Logger logger = LoggerFactory.getLogger(EventListenerMethod.class);

	public EventListenerMethod(Object target, Method method, EventListenerArgumentResolverComposite argumentResolvers) {
		Assert.isNull(target, "Target is required");
		Assert.isNull(method, "Method is required");
		this.argumentResolvers = argumentResolvers;
		this.target = target;
		this.targetType = ClassUtils.getUserClass(target);
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
	}

	public EventListenerArgumentResolverComposite getArgumentResolvers() {
		return argumentResolvers;
	}

	public void setArgumentResolvers(EventListenerArgumentResolverComposite argumentResolvers) {
		this.argumentResolvers = argumentResolvers;
	}

	public Object getTarget() {
		return target;
	}

	public Class<?> getTargetType() {
		return targetType;
	}

	public Method getMethod() {
		return method;
	}

	public Method getBridgedMethod() {
		return bridgedMethod;
	}

	public MethodParameter[] getParameters() {
		return parameters;
	}

	public Object invoke(final CanalMessage canalMessage) throws Exception {
		Object[] args = getMethodArgumentValues(canalMessage);

		if(logger.isTraceEnabled()){
			logger.trace("Arguments: {}", Arrays.toString(args));
		}

		return doInvoke(args);
	}

	private Object[] getMethodArgumentValues(final CanalMessage canalMessage) throws Exception {
		MethodParameter[] parameters = getParameters();

		if(Validate.isEmpty(parameters)){
			return EMPTY_ARGS;
		}

		final Object[] args = new Object[parameters.length];

		for(int i = 0; i < parameters.length; i++){
			MethodParameter parameter = parameters[i];

			Assert.isFalse(argumentResolvers.supports(parameter),
					()->new IllegalStateException(formatArgumentError(parameter, "No suitable resolver")));

			try{
				args[i] = argumentResolvers.resolve(parameter, canalMessage);
			}catch(Exception e){
				if(logger.isDebugEnabled()){
					String exMsg = e.getMessage();
					if(exMsg != null && exMsg.contains(parameter.getMethod().toGenericString()) == false){
						logger.debug(formatArgumentError(parameter, exMsg));
					}
				}
				throw e;
			}
		}

		return args;
	}

	private Object doInvoke(Object... args) throws Exception {
		ReflectionUtils.makeAccessible(getBridgedMethod());
		return getBridgedMethod().invoke(getTarget(), args);
	}

	private MethodParameter[] initMethodParameters() {
		Parameter[] parameters = method.getParameters();
		MethodParameter[] result = new MethodParameter[parameters.length];

		for(int i = 0; i < parameters.length; i++){
			result[i] = new MethodParameter(parameters[i].getName(), method, i, parameters[i], target.getClass());
		}

		return result;
	}

	private static String formatArgumentError(final MethodParameter methodParameter, final String message) {
		final StringBuilder sb = new StringBuilder("Could not resolve parameter [");

		sb.append('[').append(methodParameter.getIndex()).append(']');
		sb.append(" in ").append(methodParameter.getMethod().toGenericString());

		if(Validate.hasText(message)){
			sb.append(": ").append(message);
		}

		return sb.toString();
	}

}
