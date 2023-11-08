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

import com.buession.canal.core.binding.CanalBinding;
import com.buession.core.utils.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;

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

		object.setListeners(new ArrayList<>());

		CanalEventListenerAnnotationUtils.applyListener(getDestination(), getObjectType(), object);
	}

}
