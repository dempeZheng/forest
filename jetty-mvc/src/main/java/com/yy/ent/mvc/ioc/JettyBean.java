package com.yy.ent.mvc.ioc;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class JettyBean {

    private Map<String,PathBean> pathBean = new HashMap<String,PathBean>();

    private Map<String,Bean> beanMap = new LinkedHashMap<String,Bean>();

    private Set<PackageBean> packageBean = new HashSet<PackageBean>();

    public Map<String, PathBean> getPathBean() {
        return pathBean;
    }

    public void setPathBean(String id, PathBean pathBean) {
        this.pathBean.put(id, pathBean);
    }

    public Map<String, Bean> getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(String id, Bean bean) {
        this.beanMap.put(id, bean);
    }

    public Set<PackageBean> getPackageBean() {
        return packageBean;
    }

    public void setPackageBean(PackageBean packageBean) {
        this.packageBean.add(packageBean);
    }

}
