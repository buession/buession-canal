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
package com.buession.canal.core.exception;

import com.alibaba.otter.canal.protocol.exception.CanalClientException;

/**
 * @author Yong.Teng
 * @since 0.0.1
 */
public class BindingException extends CanalClientException {

	private final static long serialVersionUID = -283975971655388701L;

	public BindingException(String errorCode) {
		super(errorCode);
	}

	public BindingException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public BindingException(String errorCode, String errorDesc) {
		super(errorCode, errorDesc);
	}

	public BindingException(String errorCode, String errorDesc, Throwable cause) {
		super(errorCode, errorDesc, cause);
	}

	public BindingException(Throwable cause) {
		super(cause);
	}

}
