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
package com.buession.canal.spring.annotation.factory;

import com.buession.canal.annotation.CanalBinding;
import com.buession.canal.annotation.CanalEventListener;
import com.buession.canal.client.dispatcher.AbstractDispatcher;
import com.buession.canal.core.listener.utils.EventListenerUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBindingBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		CanalBinding canalBinding = AnnotationUtils.findAnnotation(bean.getClass(), CanalBinding.class);

		if(canalBinding != null){
			AbstractDispatcher dispatcher = beanFactory.getBean(AbstractDispatcher.class);

			ReflectionUtils.doWithMethods(bean.getClass(), (method)->{
				CanalEventListener canalEventListener = AnnotatedElementUtils.findMergedAnnotation(method,
						CanalEventListener.class);

				if(canalEventListener != null){
					Method invocableMethod = AopUtils.selectInvocableMethod(method, bean.getClass());
					detectBindingMethod(bean, invocableMethod, canalEventListener, dispatcher,
							canalBinding.destination());
				}
			});
		}

		return bean;
	}

	protected void detectBindingMethod(final Object bean, final Method method,
									   final CanalEventListener canalEventListener,
									   final AbstractDispatcher dispatcher, final String destination) {
		final String listenerName = EventListenerUtils.buildEventListenerName(destination,
				canalEventListener.schema(), canalEventListener.table(), canalEventListener.eventType());
		dispatcher.getEventListenerRegistry().register(listenerName, bean, method);
	}

}
