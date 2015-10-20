package com.yy.ent.ioc;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/19
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public class PackageUtils {
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
