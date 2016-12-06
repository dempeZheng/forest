package com.dempe.forest;

import com.dempe.forest.client.proxy.RpcProxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/6 0006
 * Time: 下午 9:06
 * To change this template use File | Settings | File Templates.
 */
public class Forest {

    final static RpcProxy rpcProxy = new RpcProxy(new RefConfMapping());

    public static <T> T from(Class<T> clazz) {
        return rpcProxy.getProxy(clazz);
    }
//    public static Forest registerMethodOption(String methodName,MethodOption methodOption){
//        rpcProxy.setMethodOption(methodName, methodOption);
//        return this;
//
//    }

   public static void createServer(){

   }
}
