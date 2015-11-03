package com.yy.ent.mvc.ioc;

import com.yy.ent.common.utils.PackageUtils;
import com.yy.ent.mvc.anno.Exclude;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/19
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private static Map<String, Object> beanMap = new HashMap<String, Object>();

    public static void init(String... packages) {
        Set<String> clazzs = new HashSet<String>();
        for (String perPak : packages) {
            String[] classes = PackageUtils.findClassesInPackage(perPak);
            Collections.addAll(clazzs, classes);
        }
        BeanFactory.register(clazzs);
    }

    public static void register(Set<String> clazzs) {
        for (String clazzName : clazzs) {
            register(clazzName);
        }
        show();
    }

    public static void register(String clazzName) {
        register(clazzName, null);
    }


    public static void register(String clazzName, String id) {
        if (exist(clazzName))
            return;
        try {
            log.debug("invoke class for name: {} ", clazzName);
            Class<?> clazz = Class.forName(clazzName);
            log.debug("invoke class for name over");

            //如果是注解Exclude就不管理
            Exclude beanAnnotation = (Exclude) clazz.getAnnotation(Exclude.class);
            if (beanAnnotation != null)
                return;

            //逆名类
            if (clazz.isAnonymousClass()) {
                return;
            }

            Object bean = null;
            boolean useDefaultConstructor = false;//是否默认用无参的构造方法
            Constructor<?>[] css = clazz.getConstructors();
            for (Constructor<?> cs : css) {
                Class<?>[] pss = cs.getParameterTypes();
                if (pss == null || pss.length == 0) {
                    useDefaultConstructor = true;
                    break;
                }
            }
            if (useDefaultConstructor) {
                bean = clazz.newInstance();
            } else {
                if (css.length > 1) {
                    log.error("BeanFactory register error when initiating {}, not confirm which constructor", clazzName);
                    return;
                } else {
                    Constructor<?> cc = css[0];
                    Class<?>[] params = cc.getParameterTypes();
                    Object[] values = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        Class<?> param = params[i];
                        Object obj = getBeanByClass(param);
                        if (obj == null) {
                            log.error("BeanFactory register error when initiating {}, constructor init error ,not constructor param object", clazzName);
                            return;
                        } else {
                            values[i] = obj;
                        }
                    }
                    bean = cc.newInstance(values);
                }
            }
            beanMap.put(clazzName, bean);
            if (StringUtils.isNotBlank(id)) {
                beanMap.put(getBeanKey(clazzName, id), bean);
            }

            Injector.doInject(bean);

        } catch (Exception e) {
            log.error("BeanFactory register error when initiating " + clazzName, e);
        }
    }

    public static String getBeanKey(String clazzName, String id) {
        if (StringUtils.isBlank(id)) {
            return clazzName;
        }
        return clazzName + "_" + id;
    }

    public static void prepareAl(Map<String, Object> map) {
        beanMap.putAll(map);
    }

    public static void prepare(String key, Object value) {
        beanMap.put(key, value);
    }

    public static void show() {
        for (String key : beanMap.keySet()) {
            log.debug(key + ":" + beanMap.get(key));
        }
    }

    /**
     * 根据关键字获取实例
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String key) {
        return (T) beanMap.get(key);
    }

    /**
     * 查找某个类型的对象
     *
     * @param clazz
     * @return
     */
    public static Object getBeanByClass(Class<?> clazz) {
        if (beanMap == null || beanMap.size() <= 0) {
            return null;
        }
        List<Object> list = new ArrayList<Object>();
        for (Object obj : beanMap.values()) {
            //找类型一致的对象
            if (clazz.isAssignableFrom(obj.getClass())) {
                list.add(obj);
            }
        }
        if (list != null && list.size() > 0) {
            int step = Integer.MAX_VALUE;
            Object suitMax = null;
            for (Object obj : list) {
                int st = 0;
                Class<?> cs = obj.getClass();
                //遍历找层次最近的
                while (cs != Object.class && clazz.isAssignableFrom(cs)) {
                    if (cs.getName().equals(clazz.getName())) {
                        if (st < step) {
                            step = st;
                            suitMax = obj;
                        }
                        break;
                    } else {
                        cs = cs.getSuperclass();
                    }
                    st++;
                }
            }
            if (suitMax != null) {
                return suitMax;
            } else {
                return list.get(0);
            }

        }
        return null;
    }

    public static boolean exist(String key) {
        return beanMap.containsKey(key);
    }

}
