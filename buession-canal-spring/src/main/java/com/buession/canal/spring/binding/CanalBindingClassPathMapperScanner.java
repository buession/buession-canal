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
package com.buession.canal.spring.mapper;

import com.buession.canal.annotation.CanalBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBindingClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

	private Class<? extends CanalBindingMapperFactoryBean> canalBindingMapperFactoryBeanClass = CanalBindingMapperFactoryBean.class;

	private boolean lazyInitialization;

	private final static Logger logger = LoggerFactory.getLogger(CanalBindingClassPathMapperScanner.class);

	public CanalBindingClassPathMapperScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
		addIncludeFilter();
	}

	public CanalBindingClassPathMapperScanner(BeanDefinitionRegistry registry, Environment environment) {
		super(registry, false, environment);
		addIncludeFilter();
	}

	public CanalBindingClassPathMapperScanner(BeanDefinitionRegistry registry, Environment environment,
											  ResourceLoader resourceLoader) {
		super(registry, false, environment, resourceLoader);
		addIncludeFilter();
	}

	public void setCanalBindingMapperFactoryBeanClass(
			Class<? extends CanalBindingMapperFactoryBean> canalBindingMapperFactoryBeanClass) {
		if(canalBindingMapperFactoryBeanClass != null){
			this.canalBindingMapperFactoryBeanClass = canalBindingMapperFactoryBeanClass;
		}
	}

	public void setLazyInitialization(boolean lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		final AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
		//return annotationMetadata.isIndependent() && annotationMetadata.isAnnotation() == false;
		return annotationMetadata.isInterface() && annotationMetadata.isIndependent();
	}

	@Override
	protected boolean checkCandidate(@NonNull String beanName, @NonNull BeanDefinition beanDefinition) {
		if(super.checkCandidate(beanName, beanDefinition)){
			return true;
		}else{
			if(logger.isDebugEnabled()){
				logger.warn("Skipping MapperFactoryBean with name '{}' and '{}' mapperInterface" +
						". Bean already defined with the same name!", beanName, beanDefinition.getBeanClassName());
			}
			return false;
		}
	}

	protected void addIncludeFilter() {
		addIncludeFilter(new AnnotationTypeFilter(CanalBinding.class));
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if(beanDefinitions.isEmpty()){
			if(logger.isDebugEnabled()){
				logger.debug("No Canal binding was found in '{}' package. Please check your configuration.",
						Arrays.toString(basePackages));
			}
		}else{
			processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	private void processBeanDefinitions(final Set<BeanDefinitionHolder> beanDefinitions) {
		BeanDefinitionRegistry registry = getRegistry();
		AbstractBeanDefinition definition;

		for(BeanDefinitionHolder holder : beanDefinitions){
			definition = (AbstractBeanDefinition) holder.getBeanDefinition();

			boolean scopedProxy = false;

			if(ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())){
				definition = (AbstractBeanDefinition) Optional
						.ofNullable(((RootBeanDefinition) definition).getDecoratedDefinition())
						.map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(()->new IllegalStateException(
								"The target bean definition of scoped proxy bean not found. Root bean definition[" +
										holder + "]"));
				scopedProxy = true;
			}

			String beanClassName = definition.getBeanClassName();
			if(logger.isDebugEnabled()){
				logger.debug("Creating MapperFactoryBean with name '{}' and '{}' mapperInterface",
						holder.getBeanName(), beanClassName);
			}

			definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
			definition.setBeanClass(canalBindingMapperFactoryBeanClass);
			definition.setLazyInit(lazyInitialization);
			definition.setAttribute("canalBindingsRegistrarFactoryBean", beanClassName);

			if(logger.isDebugEnabled()){
				logger.debug("Enabling autowire by type for MapperFactoryBean with name '{}'.",
						holder.getBeanName());
			}
			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

			if(scopedProxy){
				continue;
			}

			if(!definition.isSingleton()){
				BeanDefinitionHolder proxyHolder = ScopedProxyUtils.createScopedProxy(holder, registry, true);
				if(registry.containsBeanDefinition(proxyHolder.getBeanName())){
					registry.removeBeanDefinition(proxyHolder.getBeanName());
				}
				registry.registerBeanDefinition(proxyHolder.getBeanName(), proxyHolder.getBeanDefinition());
			}
		}
	}

}
