

package com.yy.ent.mvc.ioc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class JettyIOC {

    private static final Logger LOGGER = LoggerFactory.getLogger(JettyIOC.class);

    private JettyBean cherryBean;

    private JettyIOCContext loadContext;

    private String configFile = "cherry.xml";

    private HashMap<String, Object> val_map = new HashMap<String, Object>();

    private Map<String, Object> configBeans = new HashMap<String, Object>();

    public JettyIOC() throws Exception {
        init();
    }

    public JettyIOC(String configFile) throws Exception {
        this.configFile = configFile;
        init();
    }

    /**
     * 1:初使化所有自己配置的Bean
     * 2:初使化Cherrice上下文信息
     * *
     */
    public void initConfig() throws Exception {
        loadContext = new JettyIOCContext(configFile);
        this.cherryBean = loadContext.getCherryBean();
    }

    /**
     * 初使所有配置信息
     *
     * @throws Exception
     */
    public void init() throws Exception {
        initConfig();
        prepareBeans();
        injectConfigBeans();
    }


    private void injectConfigBeans() {
        for (Map.Entry<String, Object> entry : configBeans.entrySet()) {
            injectBean(entry.getValue());
        }
    }

    private void injectBean(Object bean) {
        try {
            Injector.doInject(bean);
        } catch (Exception e) {
            LOGGER.error("FAIL TO DO INJECT FOR :" + bean, e);
        }
    }

    /**
     * 把CherryBean里面的listBeans的信息,把有Bean的类全部生成实例加载到BeanFactory里面
     */
    protected void prepareBeans() {
        Map<String, Bean> beans = cherryBean.getBeanMap();
        for (String beanId : beans.keySet()) {
            Bean bean = beans.get(beanId);
            try {
                createBean(beanId, bean);
            } catch (Exception e) {
                LOGGER.error("Cherry prepareBeans beanId:" + beanId + ",bean:" + bean, e);
            }

        }
    }

    /**
     * 生成单个bean的实例
     *
     * @param beanId
     * @param bean
     * @throws Exception
     */
    protected void createBean(String beanId, Bean bean) throws Exception {
        String className = bean.clazz;
        Class<?> clazz = Class.forName(className);
        String key = clazz.getName();
        Object object = null;
        if (val_map.containsKey(beanId)) {
            return;
        }

        ConstructorBean constructorBean = bean.getConstructor();
        if (constructorBean != null) {
            Set<ParamBean> paramBeans = constructorBean.getParam();
            int paramSize = paramBeans.size();
            if (paramSize > 0) { // 有参方法
                //保存param的值
                Object[] paramValues = new Object[paramSize];
                //保存每个value的类型
                Class<?> paramTypes[] = new Class<?>[paramSize];
                setReflectParams(paramBeans, paramValues, paramTypes, beanId, className);
                Constructor<?> cst = clazz.getConstructor(paramTypes);
                object = cst.newInstance(paramValues);
            } else { // 无参方法
                object = clazz.newInstance();
            }
        } else {
            object = clazz.newInstance();
        }
        val_map.put(beanId, object);

        Set<MethodBean> methodBeans = bean.getMethod();
        if (methodBeans.size() > 0) {
            for (MethodBean methodBean : methodBeans) {
                String methodName = methodBean.getName();
                Set<ParamBean> paramBeans = methodBean.getParam();
                int paramSize = paramBeans.size();
                if (paramSize > 0) { // 有参方法
                    //保存param的值
                    Object[] paramValues = new Object[paramSize];
                    //保存每个value的类型
                    Class<?> paramTypes[] = new Class<?>[paramSize];
                    setReflectParams(paramBeans, paramValues, paramTypes, beanId, methodName);
                    //如果有方法有参数	modify by xy key换为beanId
                    Method m = clazz.getMethod(methodName, paramTypes);
                    m.invoke(object, paramValues);
                } else { // 无参方法
                    Method m = clazz.getMethod(methodName);
                    m.invoke(object);
                }
            }
        }
        //把所有bean放入BeanFactory
        BeanFactory.prepare(key, object);
        if (StringUtils.isNotBlank(beanId)) {
            String beanKey = BeanFactory.getBeanKey(key, beanId);
            BeanFactory.prepare(beanKey, object);
        }
        configBeans.put(key, object);
    }


    /**
     * 如是反射中有参数，那么将对其设置相应的属性和值
     *
     * @param paramBeans
     * @param paramValues
     * @param paramTypes
     * @throws Exception
     */
    private void setReflectParams(Set<ParamBean> paramBeans, Object[] paramValues, Class<?> paramTypes[], String beanId, String methodName) throws Exception {
        int i = 0;
        for (ParamBean paramBean : paramBeans) {
            String param_value = paramBean.getValue();
            String ref = paramBean.getRef();
            String type = paramBean.getType();
            if (param_value == null && ref == null) {
                throw new Exception("Cherry setReflectParams params value and ref is null! beanId:" + beanId + " methodName:" + methodName);
            }

            if (type == null) {
                paramTypes[i] = String.class;
                paramValues[i] = param_value;
            } else if (type.equals("int")) {
                paramTypes[i] = int.class;
                paramValues[i] = Integer.parseInt(param_value);
            } else if (type.equals("long")) {
                paramTypes[i] = long.class;
                paramValues[i] = Long.parseLong(param_value);
            } else if (type.equals("boolean")) {
                paramTypes[i] = boolean.class;
                paramValues[i] = Boolean.parseBoolean(param_value);
            } else if (type.equals("float")) {
                paramTypes[i] = float.class;
                paramValues[i] = Float.parseFloat(param_value);
            } else if (type.equals("short")) {
                paramTypes[i] = short.class;
                paramValues[i] = Short.parseShort(param_value);
            } else {
                Class<?> temp = Class.forName(type);
                if (temp.isEnum()) {
                    Object[] objs = temp.getEnumConstants();
                    for (Object obj : objs) {
                        if (param_value.equals(obj.toString())) {
                            paramValues[i] = obj;
                        }
                    }
                } else {
                    //目前没用实际用到待定
                }
                paramTypes[i] = temp;
            }

            if (ref != null) {
                if (ref.startsWith("{") && ref.endsWith("}")) {
                    String temp_id = ref.substring(1, ref.length() - 1);
                    //如果map里面没有这个关连id的值,那么就主动生成
                    if (!val_map.containsKey(temp_id)) {
                        //如果是PathBean
                        if (cherryBean.getPathBean().containsKey(temp_id)) {
                            PathBean bean = cherryBean.getPathBean().get(temp_id);
                            String value = bean.getValue();
                            val_map.put(temp_id, value);
                        } else if (cherryBean.getBeanMap().containsKey(temp_id)) { //如果是Bean
                            Bean bean = cherryBean.getBeanMap().get(temp_id);
                            createBean(temp_id, bean);
                        } else {
                            throw new Exception("Cherry setReflectParams param ref:{" + temp_id + "} is not declare,beanId:" + beanId + " methodName:" + methodName);
                        }
                    }
                    paramValues[i] = val_map.get(temp_id);
                } else {
                    Object obj = BeanFactory.getBean(ref);
                    if (obj == null) {
                        throw new Exception("Cherry setReflectParams param ref: BeanFactory getbean is not exist , beanId:" + beanId + " methodName:" + methodName + ",ref:" + ref);
                    }
                    paramValues[i] = obj;
                }
            }
            i++;
        }
    }
}
