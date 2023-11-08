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

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.buession.canal.core.event.EventMethod;
import com.buession.canal.core.event.invoker.DefaultEventMethodInvoker;
import com.buession.canal.core.event.invoker.EventMethodInvoker;
import com.buession.canal.core.event.invoker.PlainEventMethodInvoker;
import com.buession.canal.core.exception.ExceptionUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * {@link com.buession.canal.annotation.CanalBinding} 实例代理类
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public class Invoker implements InvocationHandler, Serializable {

	private final static long serialVersionUID = 4769603987519131562L;

	private final static int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
			| MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

	private final static Method PRIVATE_LOOKUP_IN_METHOD;

	private final static Constructor<MethodHandles.Lookup> LOOKUP_CONSTRUCTOR;

	private final Class<?> bindingType;

	private final CanalEntry.EventType eventType;

	private final Map<Method, EventMethodInvoker> methodCache;

	static {
		Method privateLookupIn;
		try{
			privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
		}catch(NoSuchMethodException e){
			privateLookupIn = null;
		}
		PRIVATE_LOOKUP_IN_METHOD = privateLookupIn;

		Constructor<MethodHandles.Lookup> lookup = null;
		if(PRIVATE_LOOKUP_IN_METHOD == null){
			// JDK 1.8
			try{
				lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
				lookup.setAccessible(true);
			}catch(NoSuchMethodException e){
				throw new IllegalStateException(
						"There is neither 'privateLookupIn(Class, Lookup)' nor 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.",
						e);
			}catch(Exception e){
				lookup = null;
			}
		}
		LOOKUP_CONSTRUCTOR = lookup;
	}

	public Invoker(final Class<?> bindingType, final CanalEntry.EventType eventType, final Map<Method,
			EventMethodInvoker> methodCache) {
		this.bindingType = bindingType;
		this.eventType = eventType;
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try{
			if(Object.class.equals(method.getDeclaringClass())){
				return method.invoke(this, args);
			}

			return cachedInvoker(method).invoke(proxy, method, args);
		}catch(Throwable t){
			throw ExceptionUtil.unwrapThrowable(t);
		}
	}

	private static MethodHandle getMethodHandleJava8(final Method method)
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		final Class<?> declaringClass = method.getDeclaringClass();
		return LOOKUP_CONSTRUCTOR.newInstance(declaringClass, ALLOWED_MODES).unreflectSpecial(method, declaringClass);
	}

	private static MethodHandle getMethodHandleJava9(final Method method)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> declaringClass = method.getDeclaringClass();
		return ((MethodHandles.Lookup) PRIVATE_LOOKUP_IN_METHOD.invoke(null, declaringClass,
				MethodHandles.lookup())).findSpecial(declaringClass, method.getName(),
				MethodType.methodType(method.getReturnType(), method.getParameterTypes()), declaringClass);
	}

	private EventMethodInvoker cachedInvoker(Method method) throws Throwable {
		try{
			return methodCache.computeIfAbsent(method, (m)->{
				if(m.isDefault()){
					try{
						if(PRIVATE_LOOKUP_IN_METHOD == null){
							return new DefaultEventMethodInvoker(getMethodHandleJava8(method));
						}else{
							return new DefaultEventMethodInvoker(getMethodHandleJava9(method));
						}
					}catch(Exception e){
						throw new RuntimeException(e);
					}
				}

				return new PlainEventMethodInvoker(new EventMethod(bindingType, method, eventType));
			});
		}catch(RuntimeException ex){
			Throwable cause = ex.getCause();
			throw cause == null ? ex : cause;
		}
	}

}
