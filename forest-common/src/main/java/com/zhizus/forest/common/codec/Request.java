package com.zhizus.forest.common.codec;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/7 0007
 * Time: 下午 9:14
 * To change this template use File | Settings | File Templates.
 */
public class Request implements Serializable {

    private String serviceName;

    private String methodName;

    private Object[] args;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Request() {
    }

    public Request(String serviceName, String methodName, Object[] args) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        return "Request{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
