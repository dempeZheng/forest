package com.zhizus.forest.support.spring.config;

import com.zhizus.forest.common.registry.impl.LocalServiceDiscovery;
import com.zhizus.forest.common.registry.impl.ZkServiceDiscovery;
import com.zhizus.forest.common.util.ForestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by Dempe on 2017/3/14.
 */
public class RegistryBeanDefinitionParser implements BeanDefinitionParser {

    private final static String ADDRESS = "address";

    private final static String REG_PROTOCOL = "regProtocol";

    enum RegProtocol {
        zookeeper("zookeeper", ZkServiceDiscovery.class), local("local", LocalServiceDiscovery.class);
        private String value;
        private Class<?> classType;

        RegProtocol(String value, Class<?> classType) {
            this.value = value;
            this.classType = classType;
        }

        public static Class<?> getClassTypeByName(String name) {
            if (StringUtils.equals(zookeeper.value, name)) {
                return zookeeper.classType;
            } else {
                return local.classType;
            }
        }
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String address = element.getAttribute(ADDRESS);
        Class<?> classTypeByName = RegProtocol.getClassTypeByName(element.getAttribute(REG_PROTOCOL));
        String id = ForestUtil.getBeanId(element, classTypeByName);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(classTypeByName);
        builder.addPropertyValue(ADDRESS, address);
        builder.setInitMethodName("start");
        parserContext.getRegistry().registerBeanDefinition(id, builder.getBeanDefinition());
        return null;
    }
}
