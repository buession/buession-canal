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

import com.buession.canal.spring.mapper.MapperScannerConfigurer;
import com.buession.core.validator.Validate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

	private Environment environment;

	private ResourceLoader resourceLoader;

	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		final AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(
				metadata.getAnnotationAttributes(EnableCanal.class.getName()));

		if(Validate.isNotEmpty(annotationAttributes)){
			registerCanalBindings(metadata, annotationAttributes, registry);
		}
	}

	private void registerCanalBindings(final AnnotationMetadata annotationMetadata,
									   final AnnotationAttributes annotationAttributes,
									   final BeanDefinitionRegistry registry) {
		final String beanName = generateBaseBeanName(annotationMetadata, 0);
		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
				MapperScannerConfigurer.class);

		builder.addPropertyValue("basePackage", getBasePackages(annotationAttributes));
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
	}

	private static Set<String> getBasePackages(@NonNull AnnotationAttributes annotationAttributes) {
		Set<String> basePackages = new HashSet<>();

		for(String pkg : annotationAttributes.getStringArray("basePackages")){
			if(Validate.hasText(pkg)){
				basePackages.add(pkg);
			}
		}

		return basePackages;
	}

	private static String generateBaseBeanName(AnnotationMetadata annotationMetadata, int index) {
		return annotationMetadata.getClassName() + "#" + CanalScannerRegistrar.class.getSimpleName() + "#" + index;
	}

}
