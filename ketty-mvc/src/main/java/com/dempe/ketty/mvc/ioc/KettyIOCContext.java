package com.dempe.ketty.mvc.ioc;

import com.dempe.ketty.common.utils.PackageUtils;
import com.dempe.ketty.common.xml.XmlParseByDom4j;
import com.dempe.ketty.mvc.anno.Exclude;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public class KettyIOCContext {

    private Element root = null;

    private File rootPath = null;

    private KettyBean cherryBean = new KettyBean();
    //存放xml文件中所有的id信息
    private Set<String> ids = new HashSet<String>();

    public KettyIOCContext(String configFile) throws Exception {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(configFile);
        rootPath = new File(resource.toURI());
        String filePath = rootPath.getPath();
        XmlParseByDom4j xmlParser = new XmlParseByDom4j(filePath);
        root = xmlParser.getRoot();
        loadAll();
    }

    /**
     * 加载所有信息
     *
     * @throws Exception
     */
    public void loadAll() throws Exception {
        loadPackageBean();
        loadPathBean();
        loadBeans();
    }

    /**
     * 把xml文件中的信息，加载到CherryBean 的pathBeans中
     *
     * @throws Exception
     */
    protected void loadPathBean() throws Exception {

        Element cherry = (Element) root.selectSingleNode("//cherry/paths");
        if (cherry == null) {
            return;
        }
        List<?> elements = cherry.elements(); // 得到cherry元素下的子元素集合
        // 循环遍历集合中的每一个元素
        for (Object obj : elements) {
            Element element = (Element) obj;
            String id = element.attributeValue("id");
            String value = element.attributeValue("value");
            List<?> list = element.elements(); // 得到某某元素下的子元素集合
            if (list.size() > 0) {
                continue;
            }
            if (id == null || value == null) {
                throw new Exception(
                        "CherryLoadContext loadPathBean id or value is null! id:" + id
                                + ",value:" + value);
            } else if (ids.contains(id)) {
                throw new Exception("CherryLoadContext  loadPathBean id repeat! id:" + id
                        + ",value:" + value);
            }
            // 如果是配置文件的路径
            String path = rootPath.getParent() + "/" + value;
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("CherryLoadContext loadPathBean file is not exists! id:"
                        + id + ",value:" + value + ",path:" + path);
            }
            PathBean pathBean = new PathBean();
            pathBean.setId(id);
            pathBean.setValue(path);
            cherryBean.setPathBean(id, pathBean);
        }
    }

    /**
     * 把xml文件中的信息，加载到CherryBean 的packageBeans中
     *
     * @throws ClassNotFoundException
     */
    protected void loadPackageBean() throws ClassNotFoundException {
        Element packages = (Element) root.selectSingleNode("//cherry/ioc/packages");
        if (packages == null) {
            return;
        }
        // 得到packages元素下的子元素集合
        List<?> list = packages.elements();
        // 循环遍历集合中的每一个元素 将每一个元素的元素名和值存入在map当中
        for (Object obj : list) {
            Element element = (Element) obj;
            String _packages = element.getText();
            PackageBean pack = new PackageBean();
            pack.setText(_packages);
            cherryBean.setPackageBean(pack);

            String[] classes = PackageUtils.findClassesInPackage(_packages);
            for (String clazzName : classes) {
                Class<?> clazz = Class.forName(clazzName);
                //是接口，枚举，逆名类都不处理
                if (clazz.isInterface() || clazz.isEnum() || clazz.isAnonymousClass()) {
                    continue;
                }
                //如果是注解Exclude就不管理
                Exclude beanAnnotation = clazz.getAnnotation(Exclude.class);
                if (beanAnnotation != null)
                    continue;
                Bean bean = new Bean();
                bean.setId(clazzName);
                bean.setClazz(clazzName);
                cherryBean.setBeanMap(clazzName, bean);
            }
        }
    }


    /**
     * 把xml文件中的信息，加载到CherryBean 的beans中
     *
     * @throws Exception
     */
    protected void loadBeans() throws Exception {
        //存放beans下面的class，用其判断是否有重复
        Set<String> clazzs = new HashSet<String>();
        Element beans = (Element) root.selectSingleNode("//cherry/ioc/beans");
        if (beans == null) {
            return;
        }
        List<?> list_beans = beans.elements(); // 得到beans元素下的子元素集合
        // 循环遍历集合中的每一个元素 将每一个元素的元素名和bean的class实例存入map
        for (Object obj : list_beans) {
            Element element = (Element) obj;
            String id = element.attributeValue("id");
            String className = element.attributeValue("class");
            if (id == null || className == null) {
                throw new Exception("CherryLoadContext loadBeans id or className is null! id:" + id + ",className:" + className);
            } else if (ids.contains(id)) {
                throw new Exception("CherryLoadContext loadBeans id repeat! id:" + id + ",className:" + className);
            }

            if (clazzs.contains(className)) {
                throw new Exception("CherryLoadContext loadBeans className repeat! id:" + id + ",className:" + className);
            }
            Bean bean = new Bean();
            bean.setId(id);
            bean.setClazz(className);

            //判断该bean下是否有method方法需要调用
            List<?> method = element.selectNodes("method");
            if (method != null && method.size() > 0) {
                for (Object m_obj : method) {
                    Element m_element = (Element) m_obj;
                    String method_name = m_element.attributeValue("name");
                    if (method_name == null) {
                        throw new Exception("CherryLoadContext loadBeans method name is null! id:" + id + ",className:" + className);
                    }
                    List<ParamBean> list = parseMethodParams(m_element);
                    if (className.endsWith(method_name)) {
                        ConstructorBean constructorBean = new ConstructorBean();
                        for (ParamBean p : list) {
                            constructorBean.addParam(p);
                        }
                        bean.setConstructor(constructorBean);
                    } else {
                        MethodBean methodBean = new MethodBean();
                        methodBean.setName(method_name);
                        for (ParamBean p : list) {
                            methodBean.setParam(p);
                        }
                        bean.setMethod(methodBean);
                    }

                }
            }

            //判断该bean下是否有constructor需要调用
            Node constructor = element.selectSingleNode("constructor");
            if (constructor != null) {
                ConstructorBean constructorBean = new ConstructorBean();
                List<ParamBean> list = parseMethodParams(constructor);
                for (ParamBean p : list) {
                    constructorBean.addParam(p);
                }
                bean.setConstructor(constructorBean);
            }
            cherryBean.setBeanMap(id, bean);
        }
    }

    private List<ParamBean> parseMethodParams(Node node) throws Exception {
        List<?> list_params = node.selectNodes("param");
        List<ParamBean> params = new ArrayList<ParamBean>();
        if (list_params.size() > 0) {
            for (Object p_obj : list_params) {
                ParamBean paramBean = new ParamBean();
                Element p_element = (Element) p_obj;
                String param_value = p_element.attributeValue("value");
                String ref = p_element.attributeValue("ref");
                if (param_value == null && ref == null) {
                    throw new Exception("CherryLoadContext loadBeans param value and ref is null,ref:" + ref + ",value:" + param_value);
                }
                String type = p_element.attributeValue("type");
                paramBean.setType(type);
                paramBean.setRef(ref);
                paramBean.setValue(param_value);
                params.add(paramBean);
            }
        }

        return params;
    }

    public KettyBean getCherryBean() {
        return cherryBean;
    }

}
