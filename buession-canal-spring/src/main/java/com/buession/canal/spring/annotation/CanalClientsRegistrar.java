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

import com.buession.canal.annotation.CanalClient;
import com.buession.canal.spring.factory.CanalClientFactoryBean;
import com.buession.core.validator.Validate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
class CanalClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

	private Environment environment;

	private ResourceLoader resourceLoader;

	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void setEnvironment(@NonNull Environment environment) {
		this.environment = environment;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	@Override
	public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata,
										@NonNull BeanDefinitionRegistry registry) {
		final AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(
				metadata.getAnnotationAttributes(EnableCanalClients.class.getName()));
		final Set<String> basePackages = getBasePackages(annotationAttributes);
		final Set<BeanDefinition> candidateComponents = new LinkedHashSet<>();

		final ClassPathScanningCandidateComponentProvider scanner =
				new CanalClientClassPathScanningCandidateComponentProvider(false, getEnvironment());
		scanner.setResourceLoader(getResourceLoader());

		for(String basePackage : basePackages){
			candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
		}

		for(BeanDefinition candidateComponent : candidateComponents){
			if(candidateComponent instanceof AnnotatedBeanDefinition){
				// verify annotated class is an interface
				AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
				AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
				Assert.isTrue(annotationMetadata.isInterface(),
						"@CanalClient can only be specified on an interface");

				Map<String, Object> attributes = annotationMetadata
						.getAnnotationAttributes(CanalClient.class.getCanonicalName());
				String name = getCanalClientName(attributes);

				registerCanalClient(registry, annotationMetadata, name, attributes);
			}
		}
	}

	@SuppressWarnings({"rawtypes"})
	private void registerCanalClient(final BeanDefinitionRegistry registry, final AnnotationMetadata annotationMetadata,
									 final String name, final Map<String, Object> attributes) {
		String className = annotationMetadata.getClassName();
		Class clazz = ClassUtils.resolveClassName(className, null);
		ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory
				? (ConfigurableBeanFactory) registry : null;

		CanalClientFactoryBean factoryBean = new CanalClientFactoryBean();
		factoryBean.setName(name);
		factoryBean.setBeanFactory(beanFactory);
		factoryBean.setType(clazz);

		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz, factoryBean::getObject);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		definition.setLazyInit(true);

		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
		beanDefinition.setAttribute("canalClientsRegistrarFactoryBean", factoryBean);
		beanDefinition.setPrimary(true);

		String[] qualifiers = new String[]{name + "CanalClient"};

		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, qualifiers);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}

	private static Set<String> getBasePackages(AnnotationAttributes annotationAttributes) {
		Set<String> basePackages = new HashSet<>();

		for(String pkg : annotationAttributes.getStringArray("basePackages")){
			if(Validate.hasText(pkg)){
				basePackages.add(pkg);
			}
		}

		return basePackages;
	}

	private static String getCanalClientName(Map<String, Object> client) {
		if(client == null){
			return null;
		}

		String value = (String) client.get("value");
		if(Validate.isBlank(value)){
			value = (String) client.get("name");
		}

		if(Validate.hasText(value)){
			return value;
		}

		throw new IllegalStateException("Either 'name' or 'value' must be provided in @CanalClient");
	}

}
