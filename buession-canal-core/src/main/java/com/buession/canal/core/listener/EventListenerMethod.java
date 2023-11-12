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

import com.buession.core.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalEventListenerMethod {

	private final BeanFactory beanFactory;

	private final Object target;

	private final Class<?> targetType;

	private final Method method;

	private final Method bridgedMethod;

	private final MethodParameter[] parameters;

	private final static Logger logger = LoggerFactory.getLogger(CanalEventListenerMethod.class);

	public CanalEventListenerMethod(Object target, Method method) {
		Assert.isNull(target, "Target is required");
		Assert.isNull(method, "Method is required");
		this.beanFactory = null;
		this.target = target;
		this.targetType = ClassUtils.getUserClass(target);
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
	}

	public CanalEventListenerMethod(String beanName, BeanFactory beanFactory, Method method) {
		Assert.isBlank(beanName, "Bean name is required");
		Assert.isNull(beanFactory, "BeanFactory is required");
		Assert.isNull(method, "Method is required");
		this.beanFactory = beanFactory;
		this.target = beanName;
		Class<?> targetType = beanFactory.getType(beanName);
		if(targetType == null){
			throw new IllegalStateException("Cannot resolve bean type for bean with name '" + beanName + "'");
		}
		this.targetType = ClassUtils.getUserClass(targetType);
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
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

	public Object invoke() throws Exception {
		Object[] args = getMethodArgumentValues();

		if(logger.isTraceEnabled()){
			logger.trace("Arguments: {}", Arrays.toString(args));
		}

		return doInvoke(args);
	}

	protected Object[] getMethodArgumentValues() throws Exception {
		return new Object[0];
	}

	protected Object doInvoke(Object... args) throws Exception {
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

}
