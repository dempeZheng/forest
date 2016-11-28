package com.dempe.forest.core;

import com.dempe.forest.core.annotation.URI;
import com.dempe.forest.core.invoker.ActionMethod;
import com.dempe.forest.core.invoker.InvokerWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
public class URIMapping   {

    private final static Logger LOGGER = LoggerFactory.getLogger(URIMapping.class);

    private String basePackage;

    private Map<String, InvokerWrapper> mapping = Maps.newConcurrentMap();

    public InvokerWrapper getInvokerWrapperByURI(String uri) {
        return mapping.get(uri);
    }


    private ApplicationContext context;

    public URIMapping( ApplicationContext context) {
        this.context = context;
        initMapping();
    }

    /**
     * 扫描packet下面所有的映射，初始化mapping
     */

    public void initMapping() {
        LOGGER.info("handles begin initiating");
        List<String> packages = new ArrayList<String>();

        packages.add(basePackage);
        LOGGER.info("scanned packages :{} ", packages);
        for (String scanPackage : packages) {
            LOGGER.info("begin get classes from package {} : ", scanPackage);
            String[] classNames = PackageUtils.findClassesInPackage(scanPackage + ".*"); // 目录下通配
            for (String className : classNames) {
                try {
                    Class<?> actionClass = Class.forName(className);
                    Controller action = actionClass.getAnnotation(Controller.class);
                    if (action == null) {
                        continue;
                    }
                    String actionVal = action.value();
                    if (StringUtils.isBlank(actionVal)) {
                        actionVal = StringUtils.uncapitalize(actionClass.getSimpleName());
                    }
                    LOGGER.info("registering action  :{} ", actionVal);
                    for (Method method : actionClass.getDeclaredMethods()) {
                        if (method.getModifiers() == Modifier.PUBLIC) {
                            URI refs = method.getAnnotation(URI.class);
                            if (refs != null) {
                                String pathVal = String.valueOf(refs.value());
                                if (StringUtils.isBlank(pathVal)) {
                                    pathVal = method.getName();
                                }
                                String uri = "/" + actionVal + "/" + pathVal;
                                if (mapping.containsKey(uri)) {
                                    LOGGER.warn("Method:{} declares duplicated jsonURI:{}, previous one will be overwritten", method, uri);
                                }
                                makeAccessible(method);
                                /**
                                 * 从spring ioc容器中获取相应的bean
                                 */
                                Object target = context.getBean(actionClass);
                                ActionMethod actionMethod =null;
                                LOGGER.info("[REQUEST MAPPING] = {}, jsonURI = {}", actionVal, uri);
                            }
                        }
                    }

                } catch (ClassNotFoundException e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                } catch (Exception e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                }
            }
        }
        LOGGER.info("Handles  Initialization successfully");
    }




    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

}
class PackageUtils {
    public final static List<String> EMPTY_LIST = new ArrayList<String>(0);

    /**
     * 查找指定package下的class
     *
     * @param packageName package名字，允许正则表达式
     * @return 符合要求的classes，全路径名
     * @throws java.io.IOException
     */
    public static String[] findClassesInPackage(String packageName) {
        return findClassesInPackage(packageName, EMPTY_LIST, EMPTY_LIST);
    }

    /**
     * 查找指定package下的class
     *
     * @param packageName package名字，允许正则表达式
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @return 符合要求的classes，全路径名
     * @throws java.io.IOException
     */
    public static String[] findClassesInPackage(String packageName, List<String> included, List<String> excluded) {
        String packageOnly = packageName;
        boolean recursive = false;
        // 递归判断
        if (packageName.endsWith(".*")) {
            packageOnly = packageName.substring(0, packageName.lastIndexOf(".*"));
            recursive = true;
        }

        List<String> vResult = new ArrayList<String>();
        try {
            String packageDirName = packageOnly.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                // 如果是目录结构
                if ("file".equals(protocol)) {
                    findClassesInDirPackage(packageOnly, included, excluded, URLDecoder.decode(url.getFile(), "UTF-8"),
                            recursive, vResult);
                }
                // 如果是jar结构
                else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.charAt(0) == '/') {
                            name = name.substring(1);
                        }
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            if (idx != -1) {
                                packageName = name.substring(0, idx).replace('/', '.');
                            }
                            if ((idx != -1) || recursive) {
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    includeOrExcludeClass(packageName, className, included, excluded, vResult);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        String[] result = vResult.toArray(new String[vResult.size()]);
        return result;
    }

    /**
     * 通过遍历目录的方式查找符合要求的包下的class
     *
     * @param packageName 包名，确定名
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param packagePath 包目录路径
     * @param recursive   是否递归
     * @param classes     结果集
     */
    private static void findClassesInDirPackage(String packageName, List<String> included, List<String> excluded,
                                                String packagePath, final boolean recursive, List<String> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 过滤目录和以class后缀的文件
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                // 递归处理
                findClassesInDirPackage(packageName + "." + file.getName(), included, excluded, file.getAbsolutePath(),
                        recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                includeOrExcludeClass(packageName, className, included, excluded, classes);
            }
        }
    }

    /**
     * include exclude过滤
     *
     * @param packageName 包名，确定名
     * @param className   短类名，不包含package前缀，确定名
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param classes     结果集
     */
    private static void includeOrExcludeClass(String packageName, String className, List<String> included,
                                              List<String> excluded, List<String> classes) {
        if (isIncluded(className, included, excluded)) {
            classes.add(packageName + '.' + className);
        } else {
        }
    }

    /**
     * 是否包含
     *
     * @param name     短类名，不包含package前缀，确定名
     * @param included 包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded 不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @return include-true,else-false
     */
    private static boolean isIncluded(String name, List<String> included, List<String> excluded) {
        boolean result = false;
        if (included.size() == 0 && excluded.size() == 0) {
            result = true;
        } else {
            boolean isIncluded = find(name, included);
            boolean isExcluded = find(name, excluded);
            if (isIncluded && !isExcluded) {
                result = true;
            } else if (isExcluded) {
                result = false;
            } else {
                result = included.size() == 0;
            }
        }
        return result;

    }

    private static boolean find(String name, List<String> list) {
        for (String regexpStr : list) {
            if (Pattern.matches(regexpStr, name)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        List<String> include = new ArrayList<String>();
        include.add(".*");
        String[] classes = findClassesInPackage("com.yy.ent.commons.accessdb.util.*", include, EMPTY_LIST);
        for (String clazz : classes) {
            System.out.println(clazz);
        }

        String className = "com.yy.ent.commons.accessdb.util.BlankUtil";
        Class<?> b = Class.forName(className);
    }
}

