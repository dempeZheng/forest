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

package com.dempe.forest.support;


import com.dempe.forest.core.exception.ForestBizException;

/**
 * @author maijunsheng
 * @version 创建时间：2013-6-14
 */
public class ExceptionUtil {

    /**
     * 判定是否是业务方的逻辑抛出的异常
     * <p>
     * <pre>
     * 		true: 来自业务方的异常
     * 		false: 来自框架本身的异常
     * </pre>
     *
     * @param e
     * @return
     */
    public static boolean isBizException(Exception e) {
        return e instanceof ForestBizException;
    }


}
