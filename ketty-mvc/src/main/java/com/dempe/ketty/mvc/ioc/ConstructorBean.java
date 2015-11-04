package com.dempe.ketty.mvc.ioc;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class ConstructorBean {

    private LinkedHashSet<ParamBean> param = new LinkedHashSet<ParamBean>();

    public Set<ParamBean> getParam() {
        return param;
    }

    public void addParam(ParamBean param) {
        this.param.add(param);
    }
}
