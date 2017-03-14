package com.zhizus.forest.support.spring.config;

import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.common.util.ForestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Dempe on 2017/3/14.
 */
public class InterceptorBeanDefinitionParser implements BeanDefinitionParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(InterceptorBeanDefinitionParser.class);

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        List<Element> interceptorsElementList = DomUtils.getChildElementsByTagName(element, "interceptor");
        if (interceptorsElementList != null) {
            for (Element interceptorElement : interceptorsElementList) {
                try {
                    Class<?> aClass = Class.forName(interceptorElement.getAttribute("class"));
//                        String automatch = interceptorElement.getAttribute("auto-match");
//                        String excludes = interceptorElement.getAttribute("excludes");
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
                    parserContext.getRegistry().registerBeanDefinition(ForestUtil.getBeanId(element, aClass), builder.getBeanDefinition());
                } catch (ClassNotFoundException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ForestFrameworkException("cannot parse interceptor");
                }

            }
        }
        return null;
    }
}
