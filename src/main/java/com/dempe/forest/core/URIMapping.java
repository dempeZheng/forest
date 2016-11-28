package com.dempe.forest.core;

import com.dempe.forest.core.invoker.InvokerWrapper;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
public class URIMapping {


    private Map<Short, InvokerWrapper> mapping = Maps.newConcurrentMap();

    public void initMapping() {

    }

    public InvokerWrapper getInvokerWrapperByURI(short uri) {
        return mapping.get(uri);
    }
}
