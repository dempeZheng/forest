package com.yy.ent.common.xml;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public class XmlParseByDom4j {

    private static Logger log = LoggerFactory.getLogger(XmlParseByDom4j.class); /*日志记录 */

    private Document document;
    private Element root;

    private String errorMsg;

    public Document getDocument() {
        return document;
    }

    public Element getRoot() {
        return root;
    }

    /**
     * 构造方法，指定XML文件，不校验文件的正确性.
     */
    public XmlParseByDom4j(InputSource is) {
        this(is, false);
    }

    public XmlParseByDom4j(InputSource is, boolean validate) {
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(is);
            root = document.getRootElement();
        } catch (DocumentException ex) {
            ex.printStackTrace();
            errorMsg = ex.getMessage();
            throw new RuntimeException("xml解析错误：" + ex.getMessage());
        }
    }

    /**
     * 构造方法，指定XML文件，不校验文件的正确性.
     *
     * @param xmlFile xml文件的路径
     */
    public XmlParseByDom4j(String xmlFile) {
        this(xmlFile, false);
    }

    /**
     * 构造方法，指定XML文件名，同时可以指定是否做校验.
     *
     * @param xmlFile  xml文件的路径
     * @param validate 是否校验XML文件的正确性
     */
    public XmlParseByDom4j(String xmlFile, boolean validate) {
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(new File(xmlFile));
            root = document.getRootElement();
        } catch (DocumentException ex) {
            ex.printStackTrace();
            errorMsg = ex.getMessage();
            throw new RuntimeException("配置文件解析错误：" + ex.getMessage());
        }
    }

    /**
     * 获取错误消息,没有则为null
     *
     * @return
     */
    public String getErrorMsg() {
        return errorMsg;
    }


    /**
     * 指导指定标识转换成字符串
     *
     * @param tagName
     * @return
     */
    public String toString(String tagName) {
        if (StringUtils.trim(tagName).intern() == "") {
            return root.asXML();
        } else {
            String tName = tagName.trim().replace('.', '/');
            Element et = root.element(tName);
            return et == null ? "" : et.asXML();
        }
    }


    /**
     * 解析Xml文件(主要是分析各节点)
     *
     * @param xmlFile
     */
    public static void testParseXmlFile(File xmlFile) {
        InputStream ifile = null;
        InputStreamReader ir = null;
        SAXReader reader = null;
        Document doc = null;
        try {
            ifile = new FileInputStream(xmlFile);
            ir = new InputStreamReader(ifile, "UTF-8");   //这样读会报错，如果该文件不是utf-8类型的
            reader = new SAXReader();
            doc = reader.read(ir);
        } catch (Exception ex) {
            log.info("解析Xml文件时,创建文件发生异常:" + ex.getMessage());
        }
        //File ir = new File("c:/test.xml");
        if (null == doc) {
            return;
        }
        Element root = doc.getRootElement();

        //下面的内容由具体所给定的Xml文件来决定
        String strtime = root.selectSingleNode("time").getText();  //时间戳
        Element elementDatas = (Element) root.selectSingleNode("datas"); //datas根结点
        if (null != elementDatas) {
            String strTotalTimes = elementDatas.attributeValue("totalTimes"); //总次数
            String strNowTimes = elementDatas.attributeValue("nowTimes");  //当前次数
            String appId = root.selectSingleNode("appid").getText();  //系统标记
            List listDatas = root.selectNodes("datas/data");  //
            if (elementDatas != null) {
                int size = listDatas.size();
                Element eleData = null;
                for (int k = 0; k < size; k++) {
                    eleData = (Element) listDatas.get(k);
                    String tableName = eleData.attributeValue("id");  //由具体的Xml文件决定
                    String handleType = eleData.attributeValue("operate"); //由具体的Xml文件决定
                    // todo something ...
                }
            }
        }
    }


}
