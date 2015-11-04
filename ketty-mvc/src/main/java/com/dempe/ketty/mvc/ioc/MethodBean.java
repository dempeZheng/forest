package com.dempe.ketty.mvc.ioc;

import java.util.LinkedHashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class MethodBean {

    public String name;

    public LinkedHashSet<ParamBean> param = new LinkedHashSet<ParamBean>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashSet<ParamBean> getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param.add(param);
    }

}
