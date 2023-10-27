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
package com.buession.canal.spring.beans.utils;

import com.buession.canal.annotation.CanalEventListener;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBeanDefinitionRegistryUtils {

	public static void registerCanalEventListeners(
			final List<com.buession.canal.spring.listener.CanalEventListener> listeners,
			final String destination, final Class<?> type, final Object target) {

		ReflectionUtils.doWithMethods(type, method->{
			CanalEventListener canalEventListener = AnnotationUtils.findAnnotation(method, CanalEventListener.class);

			if(canalEventListener != null){
				listeners.add(
						new com.buession.canal.spring.listener.CanalEventListener(destination, canalEventListener,
								target, checkProxy(target, method)));
			}
		});
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

}
