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
package com.buession.canal.spring.binding.factory;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.annotation.CanalEventListener;
import com.buession.canal.annotation.Destination;
import com.buession.canal.annotation.Schema;
import com.buession.canal.annotation.Table;
import com.buession.canal.core.ParameterType;
import com.buession.canal.core.binding.CanalBinding;
import com.buession.canal.core.event.invoker.EventMethodInvoker;
import com.buession.canal.core.listener.Invoker;
import com.buession.canal.core.listener.ParameterMapping;
import com.buession.core.builder.ListBuilder;
import com.buession.core.utils.Assert;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 工厂 bean
 *
 * @param <T>
 *        {@link com.buession.canal.annotation.CanalBinding} 类型
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBindingFactoryBean<T> extends CanalBindingFactory<T> implements InitializingBean,
		FactoryBean<CanalBinding<T>> {

	private CanalBinding<T> object;

	private final Map<Method, EventMethodInvoker> methodCache = new ConcurrentHashMap<>();

	/**
	 * 构造函数
	 */
	public CanalBindingFactoryBean() {
		super();
	}

	/**
	 * 构造函数
	 *
	 * @param bindingType
	 *        {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public CanalBindingFactoryBean(Class<T> bindingType) {
		super(bindingType);
	}

	/**
	 * 构造函数
	 *
	 * @param destination
	 * 		指令
	 * @param bindingType
	 *        {@link com.buession.canal.annotation.CanalBinding} 类型
	 */
	public CanalBindingFactoryBean(String destination, Class<T> bindingType) {
		super(destination, bindingType);
	}

	@Override
	public CanalBinding<T> getObject() throws Exception {
		return object;
	}

	@Override
	public Class<?> getObjectType() {
		return object == null ? CanalBinding.class : object.getClass();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(getBindingType() == null, "Property 'bindingType' is required");

		object = new CanalBinding<>(getBindingType(), BeanUtils.instantiateClass(getBindingType()),
				getDestination());

		object.setListeners(ListBuilder.empty());

		ReflectionUtils.doWithMethods(getBindingType(), method->applyListener(object, method));
	}

	private void applyListener(final CanalBinding<T> object, final Method method) {
		CanalEventListener canalEventListener = AnnotationUtils.findAnnotation(method,
				CanalEventListener.class);

		if(canalEventListener != null){
			final Method proxyMethod = checkProxy(object, method);
			final com.buession.canal.core.listener.CanalEventListener listener =
					new com.buession.canal.core.listener.CanalEventListener(getDestination(),
							new com.buession.canal.core.Table(canalEventListener.schema(), canalEventListener.table()),
							canalEventListener.eventType(), object.getInstance(), proxyMethod,
							parseParameters(proxyMethod),
							new Invoker(getObjectType(), canalEventListener.eventType(), methodCache));

			object.getListeners().add(listener);
		}
	}

	private static Method checkProxy(@NonNull Object bean, @NonNull Method methodArg) {
		Method method = methodArg;

		if(AopUtils.isJdkDynamicProxy(bean)){
			try{
				// Found a @CanalEventListener method on the target class for this JDK proxy
				// is it also present on the proxy itself?
				method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
				Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
				for(Class<?> iface : proxiedInterfaces){
					try{
						method = iface.getMethod(method.getName(), method.getParameterTypes());
						break;
					}catch(NoSuchMethodException noMethod){
						//
					}
				}
			}catch(SecurityException ex){
				ReflectionUtils.handleReflectionException(ex);
			}catch(NoSuchMethodException ex){
				throw new IllegalStateException(
						String.format("@CanalEventListener method '%s' found on bean target class '%s', "
										+ "but not found in any interface(s) for bean JDK proxy. Either "
										+ "pull the method up to an interface or switch to subclass (CGLIB) "
										+ "proxies by setting proxy-target-class/proxyTargetClass attribute to 'true'",
								method.getName(), method.getDeclaringClass().getSimpleName()), ex);
			}
		}

		return method;
	}

	private ParameterMapping[] parseParameters(final Method method) {
		final Parameter[] parameters = method.getParameters();

		if(parameters == null){
			return null;
		}

		final ParameterMapping[] parameterMappings = new ParameterMapping[parameters.length];

		for(int i = 0; i < parameters.length; i++){
			parameterMappings[i] = parseParameter(parameters[i]);
		}

		return parameterMappings;
	}

	private ParameterMapping parseParameter(final Parameter parameter) {
		final String parameterName = parameter.getName();
		final Class<?> parameterType = parameter.getType();

		if(CanalEntry.RowChange.class.isAssignableFrom(parameterType)){
			return new ParameterMapping(parameterName, parameterType, ParameterType.ROW_CHANGE);
		}else if(CanalEntry.RowData.class.isAssignableFrom(parameterType)){
			return new ParameterMapping(parameterName, parameterType, ParameterType.ROW_DATA);
		}else if(CanalEntry.EventType.class.isAssignableFrom(parameterType)){
			return new ParameterMapping(parameterName, parameterType, ParameterType.EVENT_TYPE);
		}else if(CharSequence.class.isAssignableFrom(parameterType)){
			Destination destination = AnnotationUtils.findAnnotation(parameter, Destination.class);
			if(destination != null){
				return new ParameterMapping(parameterName, parameterType, ParameterType.DESTINATION);
			}

			Schema schema = AnnotationUtils.findAnnotation(parameter, Schema.class);
			if(schema != null){
				return new ParameterMapping(parameterName, parameterType, ParameterType.SCHEMA);
			}

			Table table = AnnotationUtils.findAnnotation(parameter, Table.class);
			if(table != null){
				return new ParameterMapping(parameterName, parameterType, ParameterType.TABLE);
			}

			return new ParameterMapping(parameterName, parameterType, ParameterType.UNKNOW);
		}else{
			return new ParameterMapping(parameterName, parameterType, ParameterType.UNKNOW);
		}
	}

}
