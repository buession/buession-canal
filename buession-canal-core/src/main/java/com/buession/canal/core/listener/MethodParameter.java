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
package com.buession.canal.core.listener;

import com.buession.core.utils.Assert;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 * {@link com.buession.canal.annotation.CanalEventListener} 方法参数
 *
 * @author Yong.Teng
 * @since 0.0.1
 */
public final class MethodParameter implements Serializable {

	private final static long serialVersionUID = -3360650251060687317L;

	private final static Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

	/**
	 * 参数名称
	 */
	private volatile String name;

	/**
	 * 方法对象
	 */
	private final Method method;

	/**
	 * 参数索引
	 */
	private final int index;

	/**
	 * 参数
	 */
	private volatile Parameter parameter;

	/**
	 * 参数类型
	 */
	private volatile Class<?> parameterType;

	/**
	 * 方法所属类
	 */
	private volatile Class<?> type;

	/**
	 * 注解列表
	 */
	private volatile Annotation[] annotations;

	/**
	 * 构造函数
	 *
	 * @param name
	 * 		参数名称
	 * @param method
	 * 		方法
	 * @param index
	 * 		参数索引
	 * @param parameter
	 * 		参数
	 * @param type
	 * 		方法所属类
	 */
	public MethodParameter(final String name, final Method method, final int index, final Parameter parameter,
						   final Class<?> type) {
		Assert.isNull(method, "Method must not be null");
		this.name = name;
		this.method = method;
		this.index = index;
		this.parameter = parameter;
		this.type = type;
	}

	/**
	 * 返回参数名称
	 *
	 * @return 参数名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回方法
	 *
	 * @return 方法
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 返回参数索引
	 *
	 * @return 参数索引
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 返回参数
	 *
	 * @return 参数
	 */
	public Parameter getParameter() {
		return parameter;
	}

	/**
	 * 返回参数类型
	 *
	 * @return 参数类型
	 */
	public Class<?> getParameterType() {
		Class<?> parameterType = this.parameterType;
		if(parameterType != null){
			return parameterType;
		}

		this.parameterType = this.getMethod().getParameterTypes()[this.index];
		return this.parameterType;
	}

	/**
	 * 返回方法所属类
	 *
	 * @return 方法所属类
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * 判断参数是否含有注解
	 *
	 * @param annotationType
	 * 		注解
	 * @param <A>
	 * 		注解类型
	 *
	 * @return true / false
	 */
	public <A extends Annotation> boolean hasAnnotation(Class<A> annotationType) {
		return getAnnotation(annotationType) != null;
	}

	/**
	 * 返回注解
	 *
	 * @param annotationType
	 * 		注解
	 * @param <A>
	 * 		注解类型
	 *
	 * @return 注解
	 */
	@SuppressWarnings({"unchecked"})
	@Nullable
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		Annotation[] annotations = getAnnotations();
		for(Annotation annotation : annotations){
			if(annotationType.isInstance(annotation)){
				return (A) annotation;
			}
		}

		return null;
	}

	/**
	 * 返回所有注解
	 *
	 * @return 所有注解
	 */
	public Annotation[] getAnnotations() {
		Annotation[] annotations = this.annotations;

		if(annotations == null){
			Annotation[][] annotationArray = this.method.getParameterAnnotations();
			int index = this.index;

			this.annotations = (index >= 0 && index < annotationArray.length ?
					annotationArray[index] : EMPTY_ANNOTATION_ARRAY);
		}

		return annotations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, index, parameter, getParameterType(), type, Arrays.hashCode(getAnnotations()));
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if(obj instanceof MethodParameter){
			MethodParameter that = (MethodParameter) obj;
			return Objects.equals(name, that.getName()) && Objects.equals(parameter, that.getParameter()) &&
					Objects.equals(getParameterType(), that.getParameterType()) &&
					Objects.equals(type, that.getType()) &&
					Arrays.equals(getAnnotations(), that.getAnnotations());
		}

		return false;
	}

}
