package com.yy.ent.mvc.ioc;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class Bean {


    //存放id必需唯一
    public String id;

    //类的全路径
    public String clazz;

    //该里面生成实例时需要调用的方法
    public Set<MethodBean> method = new LinkedHashSet<MethodBean>();

    private ConstructorBean constructor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Set<MethodBean> getMethod() {
        return method;
    }

    public void setMethod(MethodBean method) {
        this.method.add(method);
    }

    public ConstructorBean getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorBean constructor) {
        this.constructor = constructor;
    }

}
