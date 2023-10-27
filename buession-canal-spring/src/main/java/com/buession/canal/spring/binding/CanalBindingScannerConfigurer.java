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

import com.buession.canal.spring.binding.CanalBindingClassPathMapperScanner;
import com.buession.core.collect.Arrays;
import com.buession.core.utils.Assert;
import com.buession.core.validator.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class CanalBindingScannerConfigurer implements ApplicationContextAware, BeanNameAware, InitializingBean,
		BeanDefinitionRegistryPostProcessor {

	private String[] basePackage;

	private Class<? extends CanalBindingMapperFactoryBean> canalBindingMapperFactoryBeanClass;

	private ApplicationContext applicationContext;

	private String beanName;

	private Boolean lazyInitialization;

	private boolean processPropertyPlaceHolders;

	public String[] getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String[] basePackage) {
		this.basePackage = basePackage;
	}

	public void addBasePackage(String[] basePackage) {
		this.basePackage = Arrays.merge(this.basePackage == null ? new String[0] : this.basePackage, basePackage);
	}

	public Class<? extends CanalBindingMapperFactoryBean> getCanalBindingMapperFactoryBeanClass() {
		return canalBindingMapperFactoryBeanClass;
	}

	public void setCanalBindingMapperFactoryBeanClass(
			Class<? extends CanalBindingMapperFactoryBean> canalBindingMapperFactoryBeanClass) {
		this.canalBindingMapperFactoryBeanClass = canalBindingMapperFactoryBeanClass;
	}

	public Boolean getLazyInitialization() {
		return lazyInitialization;
	}

	public void setLazyInitialization(Boolean lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	public boolean isProcessPropertyPlaceHolders() {
		return processPropertyPlaceHolders;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Environment getEnvironment() {
		return applicationContext.getEnvironment();
	}

	public String getBeanName() {
		return beanName;
	}

	@Override
	public void setBeanName(@NonNull String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isNull(basePackage, "Property 'basePackage' is required");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
		if(processPropertyPlaceHolders){
			processPropertyPlaceHolders();
		}

		final CanalBindingClassPathMapperScanner scanner = new CanalBindingClassPathMapperScanner(registry,
				getEnvironment(), getApplicationContext());

		scanner.setCanalBindingMapperFactoryBeanClass(canalBindingMapperFactoryBeanClass);
		if(lazyInitialization != null){
			scanner.setLazyInitialization(lazyInitialization);
		}

		scanner.scan(basePackage);
	}

	@Override
	public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = getApplicationContext().getBeansOfType(
				PropertyResourceConfigurer.class, false, false);

		if(prcs.isEmpty() == false && getApplicationContext() instanceof ConfigurableApplicationContext){
			BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext) getApplicationContext()).getBeanFactory()
					.getBeanDefinition(beanName);

			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for(PropertyResourceConfigurer prc : prcs.values()){
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();

			String basePackage = getPropertyValue(values, "basePackage");
			if(Validate.hasText(basePackage)){
				this.basePackage = StringUtils.tokenizeToStringArray(basePackage,
						ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			}

			String lazyInitialization = getPropertyValue(values, "lazyInitialization");
			if(Validate.hasText(lazyInitialization)){
				this.lazyInitialization = Boolean.parseBoolean(lazyInitialization);
			}
		}

		if(this.basePackage != null){
			for(int i = 0; i < this.basePackage.length; i++){
				this.basePackage[i] = getEnvironment().resolvePlaceholders(this.basePackage[i]);
			}
		}
	}

	private static String getPropertyValue(final PropertyValues values, final String propertyName) {
		PropertyValue property = values.getPropertyValue(propertyName);

		if(property == null){
			return null;
		}

		Object value = property.getValue();

		if(value == null){
			return null;
		}else if(value instanceof String){
			return value.toString();
		}else if(value instanceof TypedStringValue){
			return ((TypedStringValue) value).getValue();
		}else{
			return null;
		}
	}

}
