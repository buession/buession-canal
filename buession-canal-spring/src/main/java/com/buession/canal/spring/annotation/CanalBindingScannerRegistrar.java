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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link CanalBinding} bean definition 注册器
 *
 * @author Yong.Teng
 * @see BeanDefinition
 * @since 0.0.1
 */
class CanalBindingScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

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
				metadata.getAnnotationAttributes(EnableCanal.class.getName()));
		final CanalBindingClassPathMapperScanner scanner = new CanalBindingClassPathMapperScanner(registry,
				getEnvironment(), getResourceLoader());
		final Set<String> basePackages = getBasePackages(annotationAttributes);

		String lazyInitialization = annotationAttributes.getString("lazyInitialization");
		if(Validate.hasText(lazyInitialization)){
			scanner.setLazyInitialization(Boolean.parseBoolean(lazyInitialization));
		}

		if(basePackages.isEmpty()){
			basePackages.add(getDefaultBasePackage(metadata));
		}

		scanner.doScan(basePackages.toArray(new String[]{}));
	}

	private static Set<String> getBasePackages(final AnnotationAttributes annotationAttributes) {
		return Arrays.stream(annotationAttributes.getStringArray("basePackages")).filter(Validate::hasText).collect(
				Collectors.toSet());
	}

	private static String getDefaultBasePackage(final AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}

}
