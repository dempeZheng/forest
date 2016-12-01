/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dempe.forest.core.exception;


public class ForestBizException extends ForestAbstractException {
    private static final long serialVersionUID = -3491276058323309898L;

    public ForestBizException() {
        super(ForestErrorMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public ForestBizException(ForestErrorMsg forestErrorMsg) {
        super(forestErrorMsg);
    }

    public ForestBizException(String message) {
        super(message, ForestErrorMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public ForestBizException(String message, ForestErrorMsg forestErrorMsg) {
        super(message, forestErrorMsg);
    }

    public ForestBizException(String message, Throwable cause) {
        super(message, cause, ForestErrorMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public ForestBizException(String message, Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(message, cause, forestErrorMsg);
    }

    public ForestBizException(Throwable cause) {
        super(cause, ForestErrorMsgConstant.BIZ_DEFAULT_EXCEPTION);
    }

    public ForestBizException(Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(cause, forestErrorMsg);
    }
}
