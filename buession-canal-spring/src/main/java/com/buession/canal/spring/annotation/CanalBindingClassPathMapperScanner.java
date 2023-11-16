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
package com.buession.canal.spring.annotation;

import com.buession.canal.annotation.CanalBinding;
import com.buession.core.validator.Validate;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * {@link CanalBinding} 扫描器
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
class CanalBindingClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

	/**
	 * 是否延迟初始化
	 */
	private boolean lazyInitialization;

	private final Set<String> destinations = new HashSet<>();

	/**
	 * 构造函数
	 *
	 * @param registry
	 *        {@link BeanDefinitionRegistry}
	 * @param environment
	 *        {@link Environment}
	 * @param resourceLoader
	 *        {@link ResourceLoader}
	 */
	public CanalBindingClassPathMapperScanner(BeanDefinitionRegistry registry, Environment environment,
											  ResourceLoader resourceLoader) {
		super(registry, false, environment, resourceLoader);
		addIncludeFilter(new AnnotationTypeFilter(CanalBinding.class));
		setBeanNameGenerator(FullyQualifiedAnnotationBeanNameGenerator.INSTANCE);
	}

	/**
	 * 设置是否延迟初始化
	 *
	 * @param lazyInitialization
	 * 		是否延迟初始化
	 */
	public void setLazyInitialization(boolean lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		final AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
		return annotationMetadata.isIndependent();
	}

	@Override
	protected void registerBeanDefinition(@NonNull BeanDefinitionHolder definitionHolder,
										  @NonNull BeanDefinitionRegistry registry) {
		super.registerBeanDefinition(definitionHolder, registry);

		AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) definitionHolder.getBeanDefinition();

		if(ScopedProxyFactoryBean.class.getName().equals(beanDefinition.getBeanClassName())){
			beanDefinition = (AbstractBeanDefinition) Optional
					.ofNullable(((RootBeanDefinition) beanDefinition).getDecoratedDefinition())
					.map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(()->new IllegalStateException(
							"The target bean definition of scoped proxy bean not found. Root bean definition[" +
									definitionHolder + ']'));
		}

		processBeanDefinition(beanDefinition);
	}

	private void processBeanDefinition(final AbstractBeanDefinition beanDefinition) {
		String beanClassName = beanDefinition.getBeanClassName();

		CanalBinding canalBinding = AnnotationUtils.findAnnotation(ClassUtils.resolveClassName(beanClassName, null),
				CanalBinding.class);
		if(canalBinding == null){
			return;
		}

		if(Validate.isBlank(canalBinding.destination())){
			throw new IllegalStateException(
					"Either 'destination' must be required in @CanalBinding for: " + beanClassName);
		}

		if(destinations.contains(canalBinding.destination())){
			throw new IllegalStateException(
					"The destination: " + canalBinding.destination() + " already exists in @CanalBinding for: " +
							beanClassName);
		}

		beanDefinition.setLazyInit(lazyInitialization);
		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, beanClassName);

		destinations.add(canalBinding.destination());
	}

}
