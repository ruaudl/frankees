package org.frankees.loader.parsing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLLoaderHelper {

	public static <T> Map<String, T> loadBeans(InputStream stream,
			Class<T> beanClass) throws SAXException, IOException,
			ParserConfigurationException, IntrospectionException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, DOMException, InvocationTargetException {

		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(stream);

		return loadBeans(document.getFirstChild(), beanClass);
	}

	public static <T> Map<String, T> loadBeans(Node parent, Class<T> beanClass)
			throws IntrospectionException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, DOMException,
			InvocationTargetException {
		Map<String, T> store = new HashMap<String, T>();

		Node node = parent.getFirstChild();
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String id = getId(node);
				T bean = loadBean(node, beanClass);
				store.put(id, bean);
			}
			node = node.getNextSibling();
		}

		return store;
	}

	public static String getId(Node node) {
		return node.getAttributes().getNamedItem("id").getNodeValue();
	}

	public static <T> T loadBean(Node node, Class<T> beanClass)
			throws IntrospectionException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, DOMException,
			InvocationTargetException {
		T store = beanClass.newInstance();

		Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			properties.put(property.getName(), property);
		}

		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			PropertyDescriptor property = properties.get(attribute
					.getNodeName());
			if (property != null
					&& property.getPropertyType().equals(String.class)
					&& property.getWriteMethod() != null) {
				property.getWriteMethod().invoke(store,
						attribute.getNodeValue());
			}
		}

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			PropertyDescriptor property = properties.get(child.getNodeName());
			if (property != null
					&& property.getPropertyType().equals(String.class)
					&& property.getWriteMethod() != null) {
				property.getWriteMethod().invoke(store, child.getTextContent());
			}
		}

		return store;
	}
}
