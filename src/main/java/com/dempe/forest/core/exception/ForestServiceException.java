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


public class ForestServiceException extends ForestAbstractException {
    private static final long serialVersionUID = -3491276058323309898L;

    public ForestServiceException() {
        super(ForestErrorMsgConstant.SERVICE_DEFAULT_ERROR);
    }

    public ForestServiceException(ForestErrorMsg forestErrorMsg) {
        super(forestErrorMsg);
    }

    public ForestServiceException(String message) {
        super(message, ForestErrorMsgConstant.SERVICE_DEFAULT_ERROR);
    }

    public ForestServiceException(String message, ForestErrorMsg forestErrorMsg) {
        super(message, forestErrorMsg);
    }

    public ForestServiceException(String message, Throwable cause) {
        super(message, cause, ForestErrorMsgConstant.SERVICE_DEFAULT_ERROR);
    }

    public ForestServiceException(String message, Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(message, cause, forestErrorMsg);
    }

    public ForestServiceException(Throwable cause) {
        super(cause, ForestErrorMsgConstant.SERVICE_DEFAULT_ERROR);
    }

    public ForestServiceException(Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(cause, forestErrorMsg);
    }
}
