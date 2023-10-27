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
package com.buession.canal.core.binding;

import com.buession.canal.core.event.Event;
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
 * @author Yong.Teng
 * @since 0.0.1
 */
public class EventProxy<T> implements InvocationHandler, Serializable {

	private final static int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
			| MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

	private final Event event;

	private final Class<T> listenerInterface;

	private final Map<Method, EventMethodInvoker> methodCache;

	private static final Method privateLookupInMethod;

	private static final Constructor<MethodHandles.Lookup> lookupConstructor;

	static {
		Method privateLookupIn;
		try{
			privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
		}catch(NoSuchMethodException e){
			privateLookupIn = null;
		}
		privateLookupInMethod = privateLookupIn;

		Constructor<MethodHandles.Lookup> lookup = null;
		if(privateLookupInMethod == null){
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
		lookupConstructor = lookup;
	}

	public EventProxy(final Event event, final Class<T> listenerInterface,
					  final Map<Method, EventMethodInvoker> methodCache) {
		this.event = event;
		this.listenerInterface = listenerInterface;
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try{
			if(Object.class.equals(method.getDeclaringClass())){
				return method.invoke(this, args);
			}
			return cachedInvoker(method).invoke(proxy, method, args, event);
		}catch(Throwable t){
			throw ExceptionUtil.unwrapThrowable(t);
		}
	}

	private static MethodHandle getMethodHandleJava8(final Method method)
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		final Class<?> declaringClass = method.getDeclaringClass();
		return lookupConstructor.newInstance(declaringClass, ALLOWED_MODES).unreflectSpecial(method, declaringClass);
	}

	private static MethodHandle getMethodHandleJava9(final Method method)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> declaringClass = method.getDeclaringClass();
		return ((MethodHandles.Lookup) privateLookupInMethod.invoke(null, declaringClass,
				MethodHandles.lookup())).findSpecial(declaringClass, method.getName(),
				MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
				declaringClass);
	}

	private EventMethodInvoker cachedInvoker(Method method) throws Throwable {
		return methodCache.computeIfAbsent(method, (m)->{
			if(!m.isDefault()){
				return new PlainEventMethodInvoker(new EventMethod(listenerInterface, method));
			}
			try{
				if(privateLookupInMethod == null){
					return new DefaultEventMethodInvoker(getMethodHandleJava8(method));
				}else{
					return new DefaultEventMethodInvoker(getMethodHandleJava9(method));
				}
			}catch(IllegalAccessException e){
				throw new RuntimeException(e);
			}catch(InstantiationException e){
				throw new RuntimeException(e);
			}catch(InvocationTargetException e){
				throw new RuntimeException(e);
			}catch(NoSuchMethodException e){
				throw new RuntimeException(e);
			}
		});
	}

	private static class DefaultEventMethodInvoker implements EventMethodInvoker {

		private final MethodHandle methodHandle;

		public DefaultEventMethodInvoker(MethodHandle methodHandle) {
			this.methodHandle = methodHandle;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args, Event event) throws Throwable {
			return methodHandle.bindTo(proxy).invokeWithArguments(args);
		}
	}

	private static class PlainEventMethodInvoker implements EventMethodInvoker {

		private final EventMethod eventMethod;

		public PlainEventMethodInvoker(EventMethod eventMethod) {
			this.eventMethod = eventMethod;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args, Event event) throws Throwable {
			return eventMethod.execute(event, args);
		}
	}

}
