package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {

    private Map<String, Object> beanMap = new HashMap<String, Object>();
    private String path;
    public ClassPathXmlApplicationContext(String path) {
        if(path == null) {
            throw new RuntimeException("IOC容器配置文件路径有错误");
        }
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            // 1创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2创建 documentBuilder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3创建Document对象
            Document document = documentBuilder.parse(is);
            // 4获取文件中所有的bean节点，将bean节点的对象添加到map中
            NodeList beanList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanList.getLength(); i++) {
                // 循环所有的bean 获取对应的属性
                Node node = beanList.item(i);
                // 判断Node的类型
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // 获取相关属性
                    Element beanElement = (Element) node;
                    String id = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    // 创建bean的实例化对象
                    Object o = Class.forName(className).getDeclaredConstructor().newInstance();
                    // 将创建的对象保存到Map中，是得名字和要调用的controller类的对象一一对应
                    beanMap.put(id, o);
                    // 截止到目前，只是创建了bean对象并添加到map中，并没有处理bean之间的依赖
                }
            }
            // 5设置bean之间的关系
            for(int i = 0; i < beanList.getLength(); i++) {
                // 获取节点
                Node node = beanList.item(i);
                // 判断节点类型 只要元素节点
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    // 如果是元素节点
                    Element beanElement = (Element) node;
                    // 获取该节点的对象
                    String id = beanElement.getAttribute("id");
                    Object beanObj = getBean(id);
                    // 从该节点的所有子节点中，找到property节点
                    NodeList childNodes = beanElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if(childNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(childNode.getNodeName())) {
                            // 获取属性名
                            Element childElement = (Element) childNode;
                            String field = childElement.getAttribute("name");
                            // 获取依赖的类
                            String refClassName = childElement.getAttribute("ref");
                            // 设置依赖 给beanObj的field属性设置依赖 依赖对象为refClassName的实例化对象
                            // 通过反射获取beanObj的field字段对象
                            Field declaredField = beanObj.getClass().getDeclaredField(field);
                            declaredField.setAccessible(true);
                            // 从beanMap中获取refClassName的实例化对象
                            Object refClassObj = getBean(refClassName);
                            // 设置字段
                            declaredField.set(beanObj, refClassObj);
                        }
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        // 通过id，从beanMap中取出对应的类的对象
        return beanMap.get(id);
    }
}
