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
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scans for interfaces that declare they are canal instances (via {@link CanalBinding} <code>@CanalBinding</code>).
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CanalBindingScannerRegistrar.class)
public @interface EnableCanal {

	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
	 * {@code @ComponentScan("org.my.pkg")} instead of {@code @ComponentScan(basePackages="org.my.pkg")}.
	 *
	 * @return the array of 'basePackages'.
	 */
	@AliasFor("basePackages")
	String[] value() default {};

	/**
	 * Base packages to scan for annotated components.
	 * <p>
	 * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
	 * <p>
	 *
	 * @return the array of 'basePackages'.
	 */
	@AliasFor("value")
	String[] basePackages() default {};

	/**
	 * 是否延迟初始化 {@link CanalBinding} bean
	 *
	 * @return true / false
	 */
	String lazyInitialization() default "";

}
