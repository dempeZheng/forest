package com.zhizus.forest.support.spring.config;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.config.MethodConfig;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import com.zhizus.forest.common.util.ForestUtil;
import com.zhizus.forest.support.spring.ForestProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

/**
 * Created by Dempe on 2017/3/14.
 */
public class RefererBeanDefinitionParser implements BeanDefinitionParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(RefererBeanDefinitionParser.class);

    public final static String INTERFACE = "interface";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        try {
            Class<?> interfaceClass = Class.forName(element.getAttribute(INTERFACE));
            String id = ForestUtil.getBeanId(element, interfaceClass);
            Map<String, MethodConfig> methodConfigMap = Maps.newHashMap();
            List<Element> elementList = DomUtils.getChildElementsByTagName(element, "method");
            for (Element ele : elementList) {
                String name = ele.getAttribute("name");
                String timeout = ele.getAttribute("timeout");
                int tout = timeout == null ? 0 : Integer.parseInt(timeout);
                MethodConfig config = MethodConfig.Builder.newBuilder()
                        .withSerializeType(SerializeType.getSerializeTypeByName(ele.getAttribute("serializeType")))
                        .withCompressType(CompressType.getCompressTypeByName(ele.getAttribute("compressType")))
                        .withTimeout(tout)
                        .build();

                config.setMethodName(name);
                methodConfigMap.put(name, config);
            }
            String registry = element.getAttribute("registry");
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ForestProxyFactoryBean.class);
            if (Strings.isNullOrEmpty(registry)) {
                builder.addPropertyValue("discovery", AbstractServiceDiscovery.DEFAULT_DISCOVERY);
            } else {
                builder.addPropertyReference("discovery", registry);
            }
            builder.addPropertyValue("methodConfigMap", methodConfigMap);
            builder.addPropertyValue("serviceInterface", interfaceClass);
            parserContext.getRegistry().registerBeanDefinition(id, builder.getBeanDefinition());
            return null;
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ForestFrameworkException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ForestFrameworkException(e.getMessage());
        }


    }
}
